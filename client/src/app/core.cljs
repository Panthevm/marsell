(ns app.core
  (:require [reagent.dom :as dom]
            [re-frame.core  :as rf]

            [frames.xhr]
            [frames.routing]

            [app.routes     :as routes]

            [app.pages.model      :as pages]
            [app.pages.home.core]
            [app.pages.login.core]



            [app.components.navbar.core  :as navbar]))

(def ^:const config
 {:server-url "http://localhost:8080"
  :client-url "http://localhost:3000"})

(rf/reg-event-fx
 ::initialize
 (fn [{db :db}]
   {:db (assoc db :config config)
    :frames.routing/init routes/routes}))

(defn content [page]
  (if page
    [page]
    [:div "Страница не найдена"]))

(defn current-page []
  (let [route (rf/subscribe [:frames.routing/current])]
    (fn []
      (let [page (->> @route :match (get @pages/pages))]
        [:<>
         [navbar/component]
         [content page]]))))

(defn ^:export mount []
  (rf/clear-subscription-cache!)
  (rf/dispatch-sync [::initialize])
  (dom/render [current-page] (js/document.getElementById "app")))
