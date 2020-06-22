(ns app.db
  (:require [clj-pg.pool :as pool]))

(def ^:const database-url "jdbc:postgresql://localhost:5432/marsell?user=panthevm&stringtype=unspecified")

(def connect {:datasource (pool/create-pool {:idle-timeout        10000
                                             :minimum-idle        1
                                             :maximum-pool-size   3
                                             :connection-init-sql "select 1"
                                             :data-source.url     database-url})})
