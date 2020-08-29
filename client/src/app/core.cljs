(ns app.core
  (:require [reagent.dom :as dom]
            [re-frame.core  :as rf]
            ;;Frames
            [frames.xhr]
            [frames.page           :as page]
            [frames.routing        :as routing]
            ;;App
            [app.routes :as routes]
            ;;Components
            [app.components.infinite-scroll.core :as infinite]))

(def ^:const config
  {:base-url   "http://localhost:8080/"
   :client-url "http://localhost:3000/"})

(rf/reg-event-fx
 ::initialize
 (fn [{db :db} _]
   {:db                  (assoc db :config config)
    :frames.routing/init routes/routes}))

(defn item [idx]
  [:div {:key   idx
         :style {:border "1px solid red"
                 :height (+ 10 idx)}}
   idx])

(defn list-group []
  [:section {:style {:max-height "300px"
                     :margin "400px"
                     :border "1px solid black"
                     :overflow-y "auto"}}
   (map
    (fn [idx]
      [infinite/item {:focus :scroll}
       [item idx]])
    (range 50))])


(defn current-page []
  (let [route (rf/subscribe [::routing/current])]
    (fn []
      (let [page   (->> @route :match (get @page/pages))
            params (->> @route :params)]
        [:<>
         [infinite/list {:id :scroll}
          [list-group]]]))))

(defn ^:export mount []
  (rf/dispatch [::initialize])
  (dom/render [current-page] (js/document.getElementById "app")))
