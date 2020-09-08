(ns app.components.input.text
  (:require [frames.form   :as form]
            [re-frame.core :as reframe]))

(defn component
  [schema path params]
  (letfn [(on-change [node]
            (reframe/dispatch-sync [::form/set-value
                                    {:params (assoc schema
                                                    :path  path
                                                    :value (.. node -target -value))}]))]
    (let [node* (reframe/subscribe [::form/node {:params (assoc schema :path path)}])]
      (fn []
        (let [node @node*]
          [:<>
           [:label.block.text-white.text-sm.font-bold.mb-2 (:label params)]
           [:input.bg-purple-900.shadow.appearance-none.rounded.w-full.py-2.px-3.text-white.leading-tight.focus:outline-none
            {:valeu     (:value node)
             :on-change on-change}]])))))
