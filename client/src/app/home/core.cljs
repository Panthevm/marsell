(ns app.home.core
  (:require [re-frame.core  :as rf]
            [app.pages      :as page]
            [app.home.model :as model]))

(page/reg-page
 model/index-page
 (let [page (rf/subscribe [::model/index])]
   (fn []
     [:div
      [:h1 "Главная страница"]
      (map-indexed
       (fn [idx item] ^{:key idx}
         [:div {:style {:padding-left "300px"}} (:name item)])
       (:data @page))])))
