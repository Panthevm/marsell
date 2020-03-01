(ns app.components.header.core)

(defn component []
  [:div.container.row.center.border-bottom
   [:section
    [:div.row.center
     [:h1.space "MARSELL"]
     [:div
      [:small.block.muted "МАГАЗИН"]
      [:small.block.muted "ДЕТСКОЙ МЕБЕЛИ"]]]]
   [:section.col.form
    [:div.input-icon
     [:input {:placeholder "Поиск"}]
     [:img.icon.pointer.search {:src "../icons/search.svg"}]]]
   [:section.col.center.row.end
    [:img.icon.pointer {:src "../icons/phone.svg"}]
    [:img.icon.pointer {:src "../icons/basket.svg"}]
    [:div.row.pointer
     [:div.pt.space
      [:span.muted "0 шт. - "] [:b "0.00 "]]
     [:img.small-icon.self-center.muted {:src "../icons/arrow-down.svg"}]]]])
