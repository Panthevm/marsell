(ns ^:figwheel-hooks app.core
  (:require [reagent.core   :as reagent]
            [re-frame.core  :as rf]

            [frames.routing :as routing]

            [app.routes     :as routes]
            [app.styles     :as styles]
            [app.pages      :as pages]

            [app.home.core]
            [app.contact.core]

            [app.components.navbar.core  :as navbar]
            [app.components.sidebar.core :as sidebar]))

(rf/reg-event-fx
 ::initialize
 (fn [db]
   {:db db
    ::routing/init routes/routes}))

(defn content [page]
  [:div.container.content
   (if page
     [page]
     [:div "Страница не найдена"])])

(defn current-page []
  (let [route (rf/subscribe [::routing/current])]
    (fn []
      (let [page (->> @route :match (get @pages/pages))]
        [:<> [:style styles/app]
         [navbar/component]
         [sidebar/component]
         [content page]]))))

(defn mount-root []
  (rf/dispatch-sync [::initialize])
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn ^:after-load re-render [] (mount-root))
