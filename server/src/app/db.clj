(ns app.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db {:dbtype "postgresql"
         :dbname "marsell"
         :host   "localhost"})
