(ns app.resources.categories)

(def table
  {:table   :categories
   :columns {:id   {:type "SERIAL PRIMARY KEY"}
             :name {:type "VARCHAR(255)"}}})
