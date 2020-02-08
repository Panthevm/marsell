(ns app.components.menu.core)

(defn component []
  [:ul.menu
   [:div.accordion
    [:label.accordion-header "Категории"]
    [:li.divider]
    [:li.menu-item "Категория "
     [:ul
      [:li "Тип 1"]
      [:li "Тип 2"]
      [:li "Тип 3"]]]
    [:li.menu-item "Категория "
     [:ul
      [:li "Тип 1"]
      [:li "Тип 2"]
      [:li "Тип 3"]]]]])
