(ns app.manifest)

(def manifest
  {:resources
   {:person [[:id       "SERIAL"                         "PRIMARY KEY"]
             [:username "VARCHAR(32)"                    "NOT NULL"]
             [:password "VARCHAR(32)"                    "NOT NULL"]
             [:resource "VARCHAR(32)" "DEFAULT 'person'" "NOT NULL"]]}

   :database {:datasource {:idle-timeout       10000
                           :minimum-idle        1
                           :maximum-pool-size   3
                           :connection-init-sql "select 1"
                           :data-source.url     "jdbc:postgresql://localhost:5432/marsell?user=panthevm&stringtype=unspecified"}}})
