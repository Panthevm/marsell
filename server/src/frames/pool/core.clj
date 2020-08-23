(ns frames.pool.core
  (:require [clojure.string :as string])
  (:import (com.zaxxer.hikari HikariConfig HikariDataSource)
           (java.util Properties)))

(defn create-pool
  [opts]
  (let [props (Properties.)]
    (doseq [[k v] opts]
      (.setProperty props (name k) (str v)))
    (-> props
        HikariConfig.
        HikariDataSource.)))
