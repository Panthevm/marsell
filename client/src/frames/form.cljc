(ns frames.form
  "https://github.com/HealthSamurai/zenform"
  (:refer-clojure :exclude [eval])
  (:require [re-frame.core :as reframe]))

(defn init
  [schema value]
  (cond
    (= :form (:type schema))
    (update schema :value
            (partial reduce-kv
                     (fn [accumulator node-name node-schema]
                       (assoc accumulator node-name
                              (init node-schema (get value node-name))))
                     {}))

    (and (= :collection (:type schema))
         value)
    (assoc schema
           :value
           (reduce-kv
            (fn [accumulator index item]
              (assoc accumulator index
                     (init (:value schema) item)))
            {} value)

           :schema (:value schema))

    :else
    (assoc schema :value value)))

(defn eval
  [node]
  (cond
    (= :form (:type node))
    (reduce-kv
     (fn [accumulator node-name node-schema]
       (assoc accumulator node-name
              (eval node-schema)))
     {} (:value node))

    (and (= :collection (:type node))
         (not-empty (:value node)))
    (reduce-kv
     (fn [accumulator index node-schema]
       (conj accumulator (eval node-schema)))
     [] (:value node))

    :else (:value node)))

(defn node-path
  [form-path path]
  (vec (concat form-path
               (->> path (interpose :value) (cons :value)))))

(reframe/reg-event-db
 ::init
 (fn [db [_ {:keys [params]}]]
   (assoc-in db (:form-path params)
             (init (:form-schema params) (:data params)))))

(reframe/reg-event-db
 ::add-collection-item
 (letfn [(next-index [coll]
           (->> coll keys (apply max) inc))]
   (fn [db [_ {:keys [params]}]]
     (update-in db (node-path (:form-path params) (:path params))
                (fn [node]
                  (assoc-in node [:value (next-index (:value node))]
                            (init (:schema node) (:value params))))))))

(reframe/reg-event-db
 ::remove-collection-item
 (fn [db [_ {:keys [params]}]]
   (update-in db (conj (node-path (:form-path params) (:path params))
                       :value)
              dissoc (:index params))))

(reframe/reg-event-db
 ::set-value
 (fn [db [_ {:keys [params]}]]
   (assoc-in db (conj (node-path (:form-path params) (:path params))
                      :value)
             (:value params))))

(reframe/reg-sub
 ::node
 (fn [db [_ {:keys [params]}]]
   (get-in db (node-path (:form-path params) (:path params)))))

(reframe/reg-sub
 ::form
 (fn [db [_ {:keys [params]}]]
   (get-in db (:form-path params))))
