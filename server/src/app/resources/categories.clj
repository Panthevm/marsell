(ns app.resources.categories)

(def table
  {:table   :categories
   :columns {:id   {:type :serial :primary true :weighti 0}
             :name {:type :text}}})
