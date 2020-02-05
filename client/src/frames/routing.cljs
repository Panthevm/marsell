(ns frames.routing
  (:require [re-frame.core  :as rf]
            [clojure.string :as str]
            [route-map.core    :as route-map]))

(defn parse-fragment [routes]
  (let [location (.. js/window -location -hash)
        path     (-> location (str/replace #"^#" ""))
        route    (route-map/match [:. path] routes)]
    {:path   path
     :params (:params route)
     :match  (:match route)}))

(rf/reg-event-fx
 ::location-changed
 (fn [{db :db} [_ routes]]
   (let [{:keys [match params] :as current} (parse-fragment routes)
         {old-match :match old-params :param} (:routing db)
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
