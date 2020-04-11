(ns app.components.navbar.core
  (:require [re-frame.core :as rf]
            [app.helpers   :as h]
            [app.components.navbar.model :as model]

            [app.components.header.core  :as header]))

(defn component []
  (let [*node    (rf/subscribe [::model/data])
        *expand? (rf/subscribe [::h/expands :navbar])]
    (fn []
      (let [node    (deref *node)
            expand? (deref *expand?)]
        [:<>
         [header/component]
         [:nav.container.none.m-block
          [:div.between.row
           [:section.row
            (map-indexed
             (fn [idx link] ^{:key idx}
               [:a.pr.muted link [:b (:title link)]])
             (:nav node))]
           [:a {:href "#/auth"} "Вход"]]
          (when expand?
            [:div.m-none.border-bottom
             (map-indexed
              (fn [idx link] ^{:key idx}
                [:div.center.px
                 [:a.pr.muted link [:b (:title link)]]])
              (:nav node))
             [:div.center.px
              [:a [:b "Обратный звонок"]]]
             [:div.center.px
              [:a [:b "Вход"]]]])]]))))
