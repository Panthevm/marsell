(ns app.components.header.core)

(defn component []
  [:div.container.m-between.border-bottom.center
   [:h1 "MARSELL"]
   [:button.m-none.btn
    [:img.icon.pointer {:src "../icons/menu.svg"}]]
   [:div.none.m-block
    [:div.row
     [:img.icon.pointer {:src "../icons/phone.svg"}]
     [:img.icon.pointer {:src "../icons/basket.svg"}]
     [:div.row.center.pointer
      [:div.space
       [:span.muted "0 шт. - "] [:b "0.00 "]]
      [:img.small-icon.muted {:src "../icons/arrow-down.svg"}]]]]])
