(ns frames.routing
  (:require [re-frame.core  :as rf]))

(def ^:const *window js/window)

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
            (when params
              (reduce
               (fn [acc [_ k v]]
                 (assoc acc (keyword k) v))
               {} (re-seq #"([^=&]+)([^&]*)?" params))))
          (parse-location [location]
            (let [[path params]  (re-seq #"[^?]+"  location)]
              {:path   (->> path (re-seq #"[^/]+") next)
               :params params}))]
    (let [fragment (parse-location (.. *window -location -hash))
          route    (match routes   (:path fragment))]
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
   (letfn [(event [] (rf/dispatch [::location-changed routes]))]
     (aset *window "onhashchange" event)
     (event))))

(rf/reg-sub
 ::current
 (fn [db]
   (:routing db)))
