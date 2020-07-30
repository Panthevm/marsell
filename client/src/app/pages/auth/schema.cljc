(ns app.pages.auth.schema
  (:require [re-frame.core :as rf]))

(rf/reg-event-fx
 :modal
 (fn [_ [_ params]]
   (prn params)))

(def evs {'action [{'event [:modal {"Save" {:params {:class "save"
                                                     :color {'path [:a :b]}}
                                            :enable? {'and
                                                      [{'not {'or [{'path [:a :b]}]}}]}
                                            'action [{'event [:close-modal {:foo 1}]}]}}]
                    'success [:close-modal]
                    'error   [:error]}]})

(defmulti tag (comp first :node))

(defn parse
  [data schema]
  (reduce
   (fn [acc [k v :as node]]
     (assoc acc k
            (cond
              (symbol? k) (tag {:node node :data data})
              (map? v)    (parse data v)
              :else       v)))
   {} schema))

(defmethod tag :default
  [{[_ value] :node data :data}]
  value)

(defmethod tag 'path
  [{[_ value] :node data :data}]
  (get-in data value))

(defmethod tag 'and
  [{[_ value] :node data :data}]
  (every? #(tag {:node (first %) :data data}) value))

(defmethod tag 'or
  [{[_ value] :node data :data}]
  (some #(tag {:node (first %) :data data}) value))

(defmethod tag 'not
  [{[_ value] :node data :data}]
  (not (tag {:node (first value) :data data})))

(defmethod tag 'event
  [{[_ value] :node data :data}]
  #(rf/dispatch (update value 1 (partial parse data))))

(defmethod tag 'action
  [{[_ values] :node data :data}]
  #(doseq [event values]
     ((tag {:node (first event) :data data}))))

(comment
  (time (parse {:a {:b true}} evs)))
