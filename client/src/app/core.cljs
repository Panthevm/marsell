(ns ^:figwheel-hooks app.core
  (:require [reagent.dom :as dom]
            [re-frame.core  :as rf]

            [frames.xhr]
            [frames.routing]

            [app.routes     :as routes]
            [app.styles     :as styles]

            [app.pages.model      :as pages]
            [app.pages.home.core]

            [app.components.navbar.core  :as navbar]))

(rf/reg-event-fx
 ::initialize
 (fn []
   {:frames.routing/init routes/routes}))

(defn content [page]
  (if page
    [page]
    [:div "Страница не найдена"]))

(defn current-page []
  (let [route (rf/subscribe [:frames.routing/current])]
    (fn []
      (let [page (->> @route :match (get @pages/pages))]
        [:<> [:style styles/app]
         [navbar/component]
         [content page]]))))

(defn ^:export mount-root []
  (rf/dispatch-sync [::initialize])
  (dom/render [current-page] (js/document.getElementById "app")))

(defn ^:after-load re-render [] (mount-root))
