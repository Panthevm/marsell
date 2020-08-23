(ns app.action.person
  (:require [clojure.java.jdbc :as jdbc]))

(defn create [{:keys [request datasource]}]
  (letfn [(insert [value]
            (jdbc/with-db-connection [connection {:datasource @datasource}]
              (jdbc/insert! connection :person value)))]
    (let [resource (-> request :body insert first)]
      {:status (if resource 201 400)
       :body   resource})))
