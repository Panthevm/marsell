(ns db
  (:require [clojure.java.jdbc :as jdbc]))

(def db {:dbtype "postgresql"
            :dbname "marsell"
            :host   "localhost"})

(def user
  [[:id :serial "PRIMARY KEY"]
   [:name "VARCHAR(32)"]])

(comment
  (jdbc/db-do-commands db
                       (jdbc/create-table-ddl :persone user))

  (jdbc/insert! db :persone {:name "Фу Бар Базович"})

  (jdbc/query db ["SELECT * FROM persone"])
  (jdbc/query db ["DELETE FROM persone"])

  (jdbc/db-do-commands db
                       (jdbc/drop-table-ddl :persone user)))
