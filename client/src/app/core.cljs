(ns app.core
  (:require [reagent.dom :as dom]
            [re-frame.core  :as rf]

            ;Frames
            [frames.xhr]
            [frames.page    :as page]
            [frames.routing :as routing]

            ;App
            [app.routes :as routes]

            ;Pages
            [app.pages.home.core]
            [app.pages.login.core]

            ;Components
            [app.components.navbar.core  :as navbar]))

(def ^:const config
  {:base-url   "http://localhost:8080/"
   :client-url "http://localhost:3000/"})

(rf/reg-event-fx
 ::initialize
 (fn [{db :db} _]
   {:db (assoc db :config config)
    :frames.routing/init routes/routes}))

(defn content [page params]
  (if page
    [page params]
    [:div "Страница не найдена"]))

(defn current-page []
  (let [route (rf/subscribe [::routing/current])]
    (fn []
      (let [page   (->> @route :match (get @page/pages))
            params (->> @route :params)]
        [:<>
         [navbar/component]
         [content page params]]))))

(defn ^:export mount []
  (rf/dispatch-sync [::initialize])
  (dom/render [current-page] (js/document.getElementById "app")))
