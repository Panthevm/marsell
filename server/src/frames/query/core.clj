(ns app.frames.query
  (:import (java.sql DriverManager)))

(def url  "jdbc:postgresql://localhost:5432/marsell")
(def user "panthevm")

#_(let [conncation (DriverManager/getConnection url user "")
      statement  (.createStatement conncation)]
  (resultset-seq (.executeQuery statement "SELECT * FROM categories")))
