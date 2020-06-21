(ns app.middleware
  (:require [cheshire.core  :as ch]))

(defn add-db
  [handler db]
  (fn [req]
    (handler (assoc req :db db))))

(defn wrap-cors
  [handler]
  (fn [request]
    (-> (handler request)
        (assoc-in [:headers "Access-Control-Allow-Origin"]  "*")
        (assoc-in [:headers "Access-Control-Allow-Methods"] "GET,PUT,POST,DELETE")
        (assoc-in [:headers "Access-Control-Allow-Headers"] "X-Requested-With,Content-Type,Cache-Control,Origin,Accept,Authorization"))))
