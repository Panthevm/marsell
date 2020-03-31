(ns app.components.header.core
  (:require [re-frame.core :as rf]
            [app.helpers   :as h]))

(defn component []
  [:header.container.m-between.border-bottom.center
   [:button.m-none.btn {:on-click #(rf/dispatch [::h/expands :navbar])}
    [:img.icon.pointer {:src "icons/menu.svg"}]]
   [:h1 "MARSELL"]
   [:button.m-none.btn
    [:img.icon.pointer {:src "icons/basket.svg"}]]
   [:div.none.m-block
    [:div.row
     [:img.icon.pointer.pr {:src "icons/phone.svg"}]
     [:img.icon.pointer.pr {:src "icons/basket.svg"}]
     [:div.row.center.pointer
      [:div.space
       [:span.muted "0 шт. - "] [:b "0.00 "]]
      [:img.small-icon.muted {:src "icons/arrow-down.svg"}]]]]])
