(ns app.components.navbar.core
  (:require [re-frame.core :as rf]
            [app.components.navbar.model :as model]

            [app.components.header.core  :as header]))

(defn component []
  (let [*node (rf/subscribe [::model/data])]
    (fn []
      (let [node (deref *node)]
        [:<>
         [header/component]
         [:nav.container.between.center.row
          [:section.navbar-section.row
           (map-indexed
            (fn [idx link] ^{:key idx}
              [:section
               [:a link [:b (:title link)]]])
            (:nav node))]
          [:section
           [:span.pointer.pr "Список желаемого	"]
           [:span.muted.space "/"]
           [:span.pointer "Вход"]]]]))))
