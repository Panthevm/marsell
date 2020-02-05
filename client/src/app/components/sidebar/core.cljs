(ns app.components.sidebar.core
  (:require [re-frame.core :as rf]

            [app.styles                   :as styles]
            [app.helpers                  :as h]
            [app.components.sidebar.model :as model]))

(def styles
  (styles/style
   [:.expand {:left "-250px"}]
   [:aside   {:position   "fixed"
              :margin-top "72px"
              :left       "0"
              :width      "250px"
              :background "white"
              :transition "all 300ms ease"
              :overflow   "hidden"}
    [:.link {:padding         "24px"
             :text-decoration "none"
             :color           "black"}
     [:&:hover {:background styles/blue}]]]))

(defn component []
  (let [*open? (rf/subscribe [::h/expands :sidebar])]
    (fn []
      (let [open? @*open?]
        [:aside (when open? {:class "expand"}) [:style styles]
         (map-indexed
          (fn [idx link] ^{:key idx}
            [:a.link.block link (:title link)])
          model/links)]))))
