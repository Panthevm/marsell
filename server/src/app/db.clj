(ns app.db
  (:require
   [clj-pg.honey :as pg]
   [clojure.java.jdbc :as jdbc]
   [clj-pg.pool :as pool]))

(def database-url "jdbc:postgresql://localhost:5432/marsell?user=postgres&password=1337&stringtype=unspecified")

(defonce ds (atom nil))

(defn connect []
  (if-let [p @ds]
    p
    (reset! ds
            {:datasource (pool/create-pool {:idle-timeout        10000
                                            :minimum-idle        1
                                            :maximum-pool-size   3
                                            :connection-init-sql "select 1"
                                            :data-source.url     database-url})})))


(def state-sql (j/create-table-ddl :state2 [[:state_id :serial "PRIMARY KEY"]
                                            [:state "CHARACTER VARYING(32)"]
                                            [:abrv "date"]]))