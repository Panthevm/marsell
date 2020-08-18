(ns app.migration
  (:require [clojure.java.jdbc     :as jdbc]
            [clojure.tools.logging :as logg]))

(defn table-exist? [connection table]
  (-> (jdbc/query connection
                  (format
                   "select count(*) from information_schema.tables
                    where table_name='%s'"
                   (name table)))
      first :count pos?))

(defn table-migration [connection [table schema]]
  (logg/info "Check exist" table "table")
  (when (not (table-exist? connection table))
    (logg/info "Create " table "table")
    (jdbc/db-do-commands connection
                         (jdbc/create-table-ddl table schema)))

  (logg/info "Success exist" table))

(defn migration [manifest]
  (logg/info "Run migration")
  (let [main-connection (-> manifest :database :main :connection)]
    (doseq [table (:resources manifest)]
      (table-migration main-connection table))))
