(ns app.actions
  (:require [clojure.java.jdbc :as jdbc]
            [app.db :as db]))

(defn create-table
  [{:keys [name columns]}]
  (jdbc/db-do-commands db/db
                       (jdbc/create-table-ddl name columns)))

(defn insert
  [table-name data]
  (jdbc/insert! db/db table-name data))

(defn query
  [sql]
  (jdbc/query db/db [sql]))


(defn drop-table
  [table-name]
  (jdbc/db-do-commands db/db
                       (str "DROP TABLE" (name table-name))))
