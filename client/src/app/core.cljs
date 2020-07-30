(ns app.core
  (:require [reagent.dom :as dom]
            [re-frame.core  :as rf]

            ;Frames
            [frames.xhr]
            [frames.page           :as page]
            [frames.routing        :as routing]
            [frames.re-frame-patch :as rfp]

            ;App
            [app.routes :as routes]

            ;Pages
            [app.pages.home.core]
            [app.pages.auth.core]

            ;Components
            [app.components.navbar.core  :as navbar]))

(def ^:const config
  {:base-url   "http://localhost:8080/"
   :client-url "http://localhost:3000/"})

(rfp/reg-event-fx
 ::initialize
 (fn [{:keys [db]}]
   {:db                  (assoc db :config config)
    :frames.routing/init routes/routes}))

(defn content [page params]
  (if page
    [page params]
    [:div "Страница не найдена"]))

(defn current-page []
  (let [route (rfp/subscribe {:id ::routing/current})]
    (fn []
      (let [page   (->> @route :match (get @page/pages))
            params (->> @route :params)]
        [:<>
         [navbar/component]
         [content page params]]))))

(defn ^:export mount []
  (rfp/dispatch-sync {:id ::initialize})
  (dom/render [current-page] (js/document.getElementById "app")))
