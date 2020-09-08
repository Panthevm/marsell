(ns app.components.flash.core
  (:require [re-frame.core :as reframe]
            [frames.flash  :as frames-flash]))

(defn component
  []
  (let [data* (reframe/subscribe [::frames-flash/data])]
    (fn []
      (let [data @data*]
        [:div.fixed.absolute.bottom-0.right-0
         (when (seq data)
           (->> data
                (map
                 (fn [[idx item]]
                   [:div.bg-purple-100.border-t-4.border-purple-500.rounded-b.text-teal-900.px-4.py-3.shadow.mb-4.mr-4
                    {:key idx}
                    [:div.flex
                     [:div.py-1]
                     [:div
                      [:p.font-bold
                       (cond
                         (= :success (:status item)) "Успех!")]
                      [:p.text-sm (:message item)]]]]))))]))))

