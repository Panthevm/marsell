(ns app.core
  (:require [reagent.dom    :as dom]
            [re-frame.core  :as rf]
            ;;Frames
            [frames.xhr     :as xhr]
            [frames.page    :as page]
            [frames.routing :as routing]
            ;;App
            [app.routes :as routes]
            ;;Components
            [app.components.navbar.core     :as c-navbar]
            [app.components.breadcrumb.core :as c-breadcrumb]))


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
        [:div.bg-purple-900.h-screen
         [c-navbar/component]
         [:div.container.mx-auto.shadow-2xl
          [c-breadcrumb/component]
          [:div.bg-purple-500.my-1.rounded-b-lg
           [:h1.py-8.px-2 "Hello World"]]]]))))


(defn ^:export mount []
  (rf/dispatch [::initialize])
  (dom/render [current-page] (js/document.getElementById "app")))
