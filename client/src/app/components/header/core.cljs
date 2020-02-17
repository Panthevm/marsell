(ns app.components.header.core
  (:require [app.styles :as style]))

(defn component []
  [:div.container.row.center
   [:section.col
    [:div.row.center
     [:h1 "MARSELL"]
     [:div
      [:small.block.muted "МАГАЗИН"]
      [:small.block.muted "ДЕТСКОЙ МЕБЕЛИ"]]]]
   [:section.col
    [:input]]
   [:section.col
    [:button "Звонок"]
    [:button "Корзина"]
    [:span "Общая сумма"]]])
