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
 (fn [{db :db}]
   {:db (assoc db :config config)
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
         [:button {:on-click #(rfp/dispatch {:id ::db :params :reg-event-db})} "event-db"]
         [:button {:on-click #(rfp/dispatch {:id ::fx})}                       "event-fx"]
         [navbar/component]
         [content page params]]))))

(defn ^:export mount []
  (rfp/dispatch-sync {:id ::initialize})
  (dom/render [current-page] (js/document.getElementById "app")))

(do ;test
  (rfp/reg-event-db
   ::db
   (fn [{:keys [db params]}]
     (assoc db :type params)))

  (rfp/reg-event-fx
   ::fx
   (fn [{:keys [params]}]
     {::rfp/dispatch {:id ::db :params :reg-event-fx}})))
