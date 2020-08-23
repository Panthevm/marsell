(ns app.action.person
  (:require [clojure.java.jdbc :as jdbc]))

(defn create [{:keys [request db-connection]}]
  (let [resource (first (jdbc/insert! db-connection :person
                                      (-> request :body)))]
    {:status (if resource 201 400)
     :body   resource}))
