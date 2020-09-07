(ns app.components.input.text)

(defn component
  [params]
  [:<>
   [:label.block.text-white.text-sm.font-bold.mb-2 (:label params)]
   [:input.bg-purple-900.shadow.appearance-none.rounded.w-full.py-2.px-3.text-gray-700.leading-tight.focus:outline-none]])
