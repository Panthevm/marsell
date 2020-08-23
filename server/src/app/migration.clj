(ns app.migration
  (:require [clojure.java.jdbc     :as jdbc]
            [clojure.tools.logging :as logg]))

(defn table-exist? [db table]
  (-> (jdbc/query connection
                  (format
                   "select count(*) from information_schema.tables
                    where table_name='%s'"
                   (name table)))
      first :count pos?))

(defn table-migration [db [table schema]]
  (logg/info "Check exist" table "table")
  (when (not (table-exist? db table))
    (logg/info "Create " table "table")
    (jdbc/db-do-commands db
      (jdbc/create-table-ddl table schema)))

  (logg/info "Success exist" table))

(defn migration [manifest db]
  (logg/info "Run migration")
  (doseq [table (:resources manifest)]
    (table-migration db table)))
