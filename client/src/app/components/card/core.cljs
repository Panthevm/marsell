(ns app.components.card.core
  (:require [app.styles :as styles]))


(def styles
  (styles/css
   [:#card {:max-width "310px"}
    [:img {:max-height "360px"
           :padding "10px"
           :max-width "270px"}]
    [:.content {:padding "10px"}]
    [:&:hover {:box-shadow (str "0px 0px 0px 2px " styles/color-3)}]
    ]))

(defn component []
  [:div#card [:style styles]
   [:div.content
    [:img {:src "https://creamshop.ru/upload/iblock/faa/faaf6e3655b7a82cd99887835e798887.jpg"}]
    [:div.pt
     [:b "Название товара"]
     [:h3 [:b "10000 "] [:span.muted " руб."]]
     ]]
   ])
