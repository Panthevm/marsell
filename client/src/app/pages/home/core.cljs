(ns app.pages.home.core
  (:require [re-frame.core        :as rf]
            [app.pages.model      :as page]
            [app.pages.home.model :as model]
            [app.components.menu.core        :as menu]
            [app.components.breadcrumbs.core :as breadcrumbs]))

(page/reg-page
 model/index-page
 (let [page (rf/subscribe [::model/index])]
   (fn []
     [:div.columns
      [:div.column.col-2.col-lg-12
       [menu/component]]
      [:div.column
       [breadcrumbs/component]
       [:h1.mt-2 "Главная страница"]
       [:div.columns
        (map-indexed
         (fn [idx item] ^{:key idx}
           [:div.column.col-4..col-lg-12.mb-2
            [:div.card.c-hand
             [:div.card-header (:name item)
              [:div.card-subtitle.text-gray "Type"]]
             ]])
         (concat (:data @page) (:data @page) (:data @page)))]]])))
