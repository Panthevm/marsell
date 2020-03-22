(ns app.pages.home.core
  (:require [re-frame.core        :as rf]
            [app.pages.model      :as page]
            [app.pages.home.model :as model]

            [app.components.card.core :as card]))
(defn items []
  [:<>
   [:div.row
    [:h3.col "Новинки"]]])

(page/reg-page
 model/index-page
 (let [page (rf/subscribe [::model/index])]
   (fn []
     [:div.container
      [items]
      [:div.row
       (map-indexed
        (fn [idx item]
          [:div.col.center ^{:key idx}
           [card/component]])
        (range 6))]])))

(page/reg-page
 model/show-page
 (let [page (rf/subscribe [::model/show])]
   (fn []
     [:div.container
      [card/component]])))
