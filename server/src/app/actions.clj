(ns app.actions
  (:require [clj-pg.honey  :as pg]
            [honeysql.core :as hsql]))

(defn -exists? [db table] (pg/table-exists? db table))
(defn -create  [db table] (pg/create-table  db table))
(defn -drop    [db table] (pg/drop-table    db table))

(defn -get [table {db :db params :params}]
  (let [q (:ilike params)]
    {:staus 200
     :body (pg/query db
                     (merge {:select [:*] :from [table]}
                            (when q {:where [:ilike (hsql/raw "resource::text") (str \% q \%)]})))}))
