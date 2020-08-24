(ns app.core
  (:require [reagent.dom :as dom]
            [re-frame.core  :as rf]

            ;Frames
            [frames.xhr]
            [frames.page           :as page]
            [frames.routing        :as routing]

            ;App
            [app.routes :as routes]))

(def ^:const config
  {:base-url   "http://localhost:8080/"
   :client-url "http://localhost:3000/"})

(rf/reg-event-fx
 ::initialize
 (fn [{db :db} _]
   {:db                  (assoc db :config config)
    :frames.routing/init routes/routes}))

(defn current-page []
  (let [route (rf/subscribe [::routing/current])]
    (fn []
      (let [page   (->> @route :match (get @page/pages))
            params (->> @route :params)]
        [:<>
         [:div "Hello"]]))))

(defn ^:export mount []
  (rf/dispatch-sync [::initialize])
  (dom/render [current-page] (js/document.getElementById "app")))
