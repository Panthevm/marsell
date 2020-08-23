(ns frames.pool.core
  (:require [clojure.string :as string])
  (:import (com.zaxxer.hikari HikariConfig HikariDataSource)
           (java.util Properties)))

(def defaults
  {:auto-commit        true
   :read-only          false
   :connection-timeout 30000
   :validation-timeout 5000
   :idle-timeout       600000
   :max-lifetime       1800000
   :minimum-idle       10
   :maximum-pool-size  10})

(defn upcase
  [s]
  (str
   (.toUpperCase (.substring s 0 1))
   (.substring s 1)))

(defn propertize
  [k]
  (let [parts (string/split (name k) #"-")]
    (str (first parts) (string/join "" (map upcase (rest parts))))))

(defn create-pool
  [opts]
  (let [props (Properties.)]
    (.setProperty props "dataSourceClassName" "org.postgresql.ds.PGSimpleDataSource")
    (doseq [[k v] (merge defaults opts)]
      (when (and k v)
        (.setProperty props (propertize k) (str v))))
    (-> props
        HikariConfig.
        HikariDataSource.)))

(defn close-pool
  [datasource]
  (.close datasource))

(defonce pool (atom nil))

(defn db [options]
  (if-let [p @pool]
    p
    (reset! pool {:datasource (create-pool options)})))
