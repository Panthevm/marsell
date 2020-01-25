(ns frames.routing
  (:require [re-frame.core  :as rf]
            [clojure.string :as str]
            [goog.events    :as gevents]))

(defn location->path [location]
  (-> location
      (str/split #"/")
      next
      (->> (mapv keyword))
      not-empty))

(defn current-fragment []
  (let [location (.. js/window -location -hash)
        path     (-> location location->path (into [:-]))]
    {:path     path}))

(rf/reg-event-fx
 ::location-changed
 (fn [{db :db} [_ routes]]
   (let [{:keys [path]} (current-fragment)]
     {:db (assoc db :routing {:match (get-in routes path)})})))

(rf/reg-fx
 ::init
 (fn [routes]
   (aset js/window "onhashchange" #(rf/dispatch [::location-changed routes]))
   (rf/dispatch [::location-changed routes])))

(rf/reg-sub
 ::current
 (fn [db]
   (:routing db)))
