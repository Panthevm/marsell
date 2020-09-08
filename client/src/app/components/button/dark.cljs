(ns app.components.buttons.dark)

(defn component
  [params]
  [:button.bg-purple-600.hover:bg-purple-500.text-white.font-bold.py-2.px-4.rounded.shadow.focus:outline-none
   {:on-click (:action params)}
   (:label params)])
