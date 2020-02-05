(ns ^:figwheel-hooks app.core
  (:require [reagent.core   :as reagent]
            [re-frame.core  :as rf]

            [frames.xhr]
            [frames.routing]

            [app.routes     :as routes]
            [app.styles     :as styles]
            [app.pages      :as pages]

            [app.home.core]
            [app.contact.core]

            [app.components.navbar.core  :as navbar]
            [app.components.sidebar.core :as sidebar]))

(rf/reg-event-fx
 ::initialize
 (fn []
   {:frames.routing/init routes/routes}))

(defn content [page]
  [:div.container.content
   (if page
     [page]
     [:div "Страница не найдена"])])

(defn current-page []
  (let [route (rf/subscribe [:frames.routing/current])]
    (fn []
      (let [page (->> @route :match (get @pages/pages))]
        [:<> [:style styles/app]
         [navbar/component]
         [sidebar/component]
         [content page]]))))

(defn ^:export mount-root []
  (rf/dispatch-sync [::initialize])
  (reagent/render [current-page] (js/document.getElementById "app")))

(defn ^:after-load re-render [] (mount-root))
