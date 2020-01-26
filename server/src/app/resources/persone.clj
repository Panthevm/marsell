(ns app.resources.persone)

(def shema
  {:name    :persone
   :columns [[:id :serial "PRIMARY KEY"]
             [:name "VARCHAR(32)"]]})
