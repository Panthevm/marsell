(ns app.components.navbar.core
  (:require [re-frame.core :as rf]
            [app.styles    :as style]
            [app.components.navbar.model :as model]

            [app.components.header.core  :as header]))

(def styles
  (style/css
   [:nav {:position        "relative"
          :padding         "1rem"}]
   [:a {:padding "25px"
        :cursor  "pointer"
        :color   style/color-2}
    [:&:hover {:color "black"}]]))

(defn component []
  (let [*node (rf/subscribe [::model/data])]
    (fn []
      (let [node (deref *node)]
        [:<>
         [header/component]
         [:nav.container.between.center.row [:style styles]
          [:section.navbar-section
           (map-indexed
            (fn [idx link] ^{:key idx}
              [:a link
               [:b (:title link)]])
            (:nav node))]
          [:section
           [:a "Список желаемого	"]
           [:span "/"]
           [:a "Вход"]]]]))))
