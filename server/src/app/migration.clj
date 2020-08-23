(ns app.migration
  (:require [clojure.java.jdbc     :as jdbc]
            [clojure.tools.logging :as logg]))

(defn table-exist? [datasource table]
  (-> (jdbc/query {:datasource @datasource}
                  (format
                   "select count(*) from information_schema.tables
                    where table_name='%s'"
                   (name table)))
      first :count pos?))

(defn table-migration [datasource [table schema]]
  (logg/info "Check exist" table "table")
  (when (not (table-exist? datasource table))
    (logg/info "Create " table "table")
    (jdbc/db-do-commands {:datasource @datasource}
      (jdbc/create-table-ddl table schema)))

  (logg/info "Success exist" table))

(defn migration [manifest datasource]
  (logg/info "Run migration")
  (doseq [table (:resources manifest)]
    (table-migration datasource table)))
