(ns app.manifest)

(def manifest
  {:resources
   {:person [[:id       "SERIAL"                         "PRIMARY KEY"]
             [:username "VARCHAR(32)"                    "NOT NULL"]
             [:password "VARCHAR(32)"                    "NOT NULL"]
             [:resource "VARCHAR(32)" "DEFAULT 'person'" "NOT NULL"]]}

   :database {:connection "jdbc:postgresql://localhost:5432/marsell?user=panthevm&stringtype=unspecified"}})
