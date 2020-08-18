(ns app.manifest)

(def manifest
  {:resources
   {:user [[:id       "SERIAL"                     "PRIMARY KEY"]
           [:username "VARCHAR(32)"                "PRIMARY KEY"]
           [:password "VARCHAR(32)"                "PRIMARY KEY"]
           [:resource "VARCHAR(32)" "DEFAULT user" "PRIMARY KEY"]]}

   :database {:main {:connection "jdbc:postgresql://localhost:5432/marsell?user=panthevm&stringtype=unspecified"}}})
