(ns app.migration
  (:require [app.actions :as a]
            (app.resources
             [categories :as categories])))

(defn migrate [db table]
  (when-not (a/-exists? db (:table table))
    (a/-create db table)))

(defn migration [db]
  (migrate db categories/table))
