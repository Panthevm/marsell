(ns frames.routing
  (:require [re-frame.core  :as rf]
            [clojure.string :as str]))

(defn pathify [path]
  (-> path (str/split #"/") next not-empty))

(defn params-node [node]
  (filter (comp vector? first) node))

(defn match [routes path]
  (loop [node              routes
         [current & other] path
         params            {}]
    (if-let [node (get node current)]
      (recur node other params)
      (if current
        (let [[[[k] node]] (params-node node)]
          (recur node other (assoc params k current)))
        {:match (:- node) :params params}))))

(defn parse-fragment [routes]
  (let [location (.. js/window -location -hash)
        path     (pathify location)
        route    (match routes path)]
    {:path   path
     :params (:params route)
     :match  (or (:match route) :-)}))

(rf/reg-event-fx
 ::location-changed
 (fn [{db :db} [_ routes]]
   (let [{:keys [match params] :as current} (parse-fragment routes)
         {old-match :match old-params :params} (:routing db)
         page-hook (cond
                     (nil? old-match)       [[match :mount]]
                     (not= old-match match) [[old-match :unmount old-params]
                                             [match     :mount   params]]
                     :else [[match :unmount params]
                            [match :mount   params]])]
     {:db         (assoc db :routing current)
      :dispatch-n page-hook})))

(rf/reg-fx
 ::init
 (fn [routes]
   (aset js/window "onhashchange" #(rf/dispatch [::location-changed routes]))
   (rf/dispatch [::location-changed routes])))

(rf/reg-sub
 ::current
 (fn [db]
   (:routing db)))
