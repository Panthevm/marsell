(ns app.migration
  (:require [clj-pg.honey  :as pg]
            [app.db        :as db]
            (app.resources
             [categories :as categories]
             [user       :as user])))

(defn migrate [db table]
  (when-not (pg/table-exists? db (:table table))
    (pg/create-table db table)))

(defn migration [db]
  (migrate db user/table)
  (migrate db categories/table))

(comment
  (pg/drop-table (db/connect) categories/table)
  (pg/create db/connect categories/table {:resource {:type "1"
                                                     :name "@"}}))
