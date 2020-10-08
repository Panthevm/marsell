(ns app.action.person
  (:require [clojure.java.jdbc :as jdbc]))

(defn registration [{:keys [request datasource]}]
  (letfn [(insert [value]
            (jdbc/with-db-connection [connection {:datasource @datasource}]
              (jdbc/insert! connection :person (select-keys value [:username :password]))))]
    (let [resource (-> request :body insert first)]
      {:status (if resource 201 400)
       :body   resource})))
