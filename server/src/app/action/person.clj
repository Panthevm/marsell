(ns app.action.person
  (:require [clojure.java.jdbc :as jdbc]))

(defn create [{:keys [request context]}]
  (let [connection (-> context :database :connection)
        resource   (first (jdbc/insert! connection :person
                                        (-> request :body)))]
    {:status (if resource 201 400)
     :body   resource}))
