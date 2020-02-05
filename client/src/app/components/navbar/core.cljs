(ns app.components.navbar.core
  (:require [re-frame.core :as rf]
            [garden.core   :as garden]

            [app.helpers :as h]))
(def styles
  (garden/css
   (list
    [:nav {:position       "fixed"
           :width          "100%"
           :z-index        "2"
           :background     "white"
           :font-weight    "bold"
           :padding        "24px"
           :letter-spacing "0.025em"}])))

(defn component []
  (let [*sidebar? (rf/subscribe [::h/expands :sidebar])]
    (fn []
      (let [sidebar? @*sidebar?]
        [:nav.between [:style styles]
         [:button {:on-click #(rf/dispatch [::h/expands :sidebar])}
          "Меню"]
         [:div
          [:button "Уведомления"]
          [:button "Корзина"]
          [:button "Аккаунт"]]]))))
