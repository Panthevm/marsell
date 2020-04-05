(ns app.components.card.core)

(defn component []
  [:div#card.rounded
   [:b.center.py "Название товара"]
   [:div.pointer
    [:img.rounded {:src "https://creamshop.ru/upload/iblock/faa/faaf6e3655b7a82cd99887835e798887.jpg"}]]
   [:div.content.text-end
    [:div.body
     [:button.btn
      [:img.icon.pointer {:src "icons/heart.svg"}]]]]])
