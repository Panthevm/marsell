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

(defn parse-fragment [routes]
  (let [location (.. js/window -location -hash)
        path     (-> location location->path (into [:-]))]
    {:path  path
     :match (get-in routes path)}))

(rf/reg-event-fx
 ::location-changed
 (fn [{db :db} [_ routes]]
   (let [{:keys [path match] :as current} (parse-fragment routes)
         old-match (get-in db [:routing :match])
         page-hook (cond
                     (nil? old-match)       [[match :mount]]
                     (not= old-match match) [[old-match :unmount]
                                             [match     :mount]])]
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
