(ns frames.form
  "https://github.com/HealthSamurai/zenform"
  (:refer-clojure :exclude [import])
  (:require [re-frame.core :as reframe]))

(defmulti import (fn [schema _] (:type schema)))

(defmethod import :form
  [node value]
  (update node :value
          (partial reduce-kv
                   (fn [accumulator field-name field-schema]
                     (assoc accumulator field-name
                            (import field-schema (get value field-name))))
                   {})))

(defmethod import :collection
  [node value]
  (assoc node
         :schema (:value node)
         :value  (reduce-kv
                  (fn [accumulator index item]
                    (assoc accumulator index
                           (import (:value node) item)))
                  {} value)))

(defmethod import :default
  [node value]
  (assoc node :value value))

(defmulti export :type)

(defmethod export :form
  [node]
  (reduce-kv
   (fn [accumulator node-name node-schema]
     (assoc accumulator node-name
            (export node-schema)))
   {} (:value node)))

(defmethod export :collection
  [node]
  (reduce-kv
   (fn [accumulator index node-schema]
     (conj accumulator (export node-schema)))
   [] (:value node)))

(defmethod export :default
  [node]
  (:value node))

(defmulti validation
  (fn [value [validation-name params]]
    validation-name))

(defmethod validation :required
  [value [validation-name params]]
  (when (empty? value)
    (:message params)))

(defn node-path
  [form-path path]
  (vec (concat form-path
               (->> path (interpose :value) (cons :value)))))

(reframe/reg-event-db
 ::init
 (fn [db [_ {:keys [params]}]]
   (assoc-in db (:form-path params)
             (import (:form-schema params) (:data params)))))

(reframe/reg-event-db
 ::add-collection-item
 (letfn [(next-index [coll]
           (->> coll keys (apply max) inc))]
   (fn [db [_ {:keys [params]}]]
     (update-in db (node-path (:form-path params) (:path params))
                (fn [node]
                  (assoc-in node [:value (next-index (:value node))]
                            (import (:schema node) (:value params))))))))

(reframe/reg-event-db
 ::remove-collection-item
 (fn [db [_ {:keys [params]}]]
   (update-in db (conj (node-path (:form-path params) (:path params))
                       :value)
              dissoc (:index params))))

(reframe/reg-event-db
 ::set-value
 (fn [db [_ {:keys [params]}]]
   (let [path       (node-path (:form-path params) (:path params))
         validators (get-in db (conj path :validators))]
     (update-in db path assoc
                :value  (:value params)
                :errors (keep (partial validation (:value params))
                              validators)))))

(reframe/reg-sub
 ::node
 (fn [db [_ {:keys [params]}]]
   (get-in db (node-path (:form-path params) (:path params)))))

(reframe/reg-sub
 ::form
 (fn [db [_ {:keys [params]}]]
   (get-in db (:form-path params))))
