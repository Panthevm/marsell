(ns app.components.form.inputs
  (:require [re-frame.core :as rf]))

(defn input
  [form-path path & [attrs]]
  (let [*node (rf/subscribe [:zf/node form-path path])]
    (letfn [(on-change [js-node]
              (let [value (.. js-node -target -value)]
                (rf/dispatch [:zf/set-value form-path path value])))]
      (fn []
        (let [node @*node]
          [:input {:defaultValue (:value node)
                   :on-change    on-change}])))))
