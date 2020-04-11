(ns frames.routing
  (:require [re-frame.core  :as rf]
            [clojure.string :as str]))

(defn match [routes path]
  (letfn [(params-node [node]
            (filter (comp vector? first) node))]
    (loop [node              routes
           [current & other] path
           params            {}]
      (if-let [node (get node current)]
        (recur node other params)
        (if current
          (let [[[[k] node]] (params-node node)]
            (recur node other (assoc params k current)))
          {:match (:- node) :params params})))))

(defn parse-fragment [routes]
  (letfn [(parse-params [params]
            (reduce
             (fn [acc query]
               (let [[k v]  (str/split query #"=" 2)]
                 (assoc acc (keyword k) v)))
             {} (str/split params "&")))
          (parse-location [location]
            (let [[path params] (str/split location #"\?")]
              {:path   (-> path (str/split #"/") next not-empty)
               :params params}))]
    (let [fragment (parse-location (.. js/window -location -hash))
          route    (match routes (:path fragment))]
      {:path   (:path fragment)
       :query  (parse-params (:params fragment))
       :params (:params route)
       :match  (or (:match route) :-)})))

(rf/reg-event-fx
 ::location-changed
 (fn [{db :db} [_ routes]]
   (letfn [(add-event [opts event]
             (update opts :dispatch-n conj event))]
     (let [{match  :match params  :params query  :query :as new} (parse-fragment routes)
           {omatch :match oparams :params oquery :query :as old} (:routing db)]
       (cond-> {:db (assoc db :routing new)}
         (=    query oquery) (add-event [match  :init   new])
         (not= query oquery) (add-event [match  :params new])
         (not= omatch match) (add-event [omatch :deinit old]))))))

(rf/reg-fx
 ::init
 (fn [routes]
   (aset js/window "onhashchange" #(rf/dispatch [::location-changed routes]))
   (rf/dispatch [::location-changed routes])))

(rf/reg-sub
 ::current
 (fn [db]
   (:routing db)))
