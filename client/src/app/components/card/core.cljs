(ns app.components.card.core)

(defn component []
  [:div#card
   [:div.content
    [:img {:src "https://creamshop.ru/upload/iblock/faa/faaf6e3655b7a82cd99887835e798887.jpg"}]
    [:div.pt
     [:b "Название товара"]
     [:h3 [:b "10000 "] [:span.muted " руб."]]
     ]]
   ])
