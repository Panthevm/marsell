(ns frames.routing
  (:require [re-frame.core :as rf]))

(defn *location-hash
  []
  #?(:cljs (.. js/window -location -hash)))

(defn- parse-search
  [search]
  (reduce
   (fn [acc [_ k _ v]]
     (assoc acc (keyword k) v))
   {} (re-seq #"([^=&]+)(=([^&]*))?" search)))

(defn- params-node
  [node]
  (->> node
       (filter (comp symbol? first))
       first))

(defn match
  ([routes location-hash]
   (let [[path search] (->> location-hash (re-seq #"[^?]+"))
         paths         (->> path          (re-seq #"[^/]+") next)
         format-search (some->> search   parse-search)]
     (match routes paths {:search format-search})))

  ([current-node [current-path & other-paths] params]
   (if-let [next-node (get current-node current-path)]
     (recur next-node other-paths params)
     (if-not current-path
       {:id (:- current-node) :params params}
       (let [[params-key node] (params-node current-node)]
         (recur node other-paths (assoc params (keyword params-key) current-path)))))))

(rf/reg-event-fx
 ::location-changed
 (fn [{db :db} [_ {:keys [params]}]]
   (letfn [(add-event [opts event]
             (update opts :dispatch-n conj event))]
     (let [{id  :id params  :params :as new} (match (:routes params) (:location params))
           {oid :id oparams :params :as old} (:routing db)]
       (cond-> {:db (assoc db :routing new)}
         (=    params oparams) (add-event [id  {:params (assoc new :phase :init)}])
         (not= params oparams) (add-event [id  {:params (assoc new :phase :params)}])
         (not= oid id)         (add-event [oid {:params (assoc new :phase :deinit)}]))))))

(rf/reg-fx
 ::init
 (letfn [(event [params]
           (rf/dispatch [::location-changed {:params params}]))]
   (fn [{:keys [params]}]
     #?(:cljs (aset js/window "onhashchange" #(event (assoc params :location (*location-hash)))))
     (event (assoc params :location (*location-hash))))))

(rf/reg-sub
 ::current-route
 (fn [db _]
   (:routing db)))
