(ns app.pages.home.core
  (:require [re-frame.core        :as rf]
            [app.pages.model      :as page]
            [app.pages.home.model :as model]

            [app.components.card.core :as card]))

(defn catalogs []
  [:div
   [:h3.col "КАТАЛОГ"]
   [:div.row
    [:div.col
     [:a.banner
      [:h2.col.caption [:b "Кровати"]]]]
    [:div.col
     [:a.banner
      [:h2.col.caption [:b "Шкафы"]]]]
    [:div.col
     [:a.banner
      [:h2.col.caption [:b "Комоды"]]]]]])

(defn items []
  [:<>
   [:div.row
    [:h3.col "Новинки"]]])

(page/reg-page
 model/index-page
 (let [page (rf/subscribe [::model/index])]
   (fn []
     [:div.container
      [catalogs]
      [items]
      [:div.row
       [:div.col
        [card/component]]
       [:div.col
        [card/component]]
       [:div.col
        [card/component]]]

      ])))
