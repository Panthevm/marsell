(ns app.middleware
  (:require [clojure.data.json   :as json]
            [clojure.string      :as str]
            [frames.routing.core :as routing]))

(defn wrap-cors
  [handler]
  (fn [data]
    (-> (handler data)
        (assoc-in [:headers "Access-Control-Allow-Origin"]  "*")
        (assoc-in [:headers "Access-Control-Allow-Methods"] "GET,PUT,POST,DELETE")
        (assoc-in [:headers "Access-Control-Allow-Headers"] "X-Requested-With,Content-Type,Cache-Control,Origin,Accept,Authorization"))))

(defn wrap-edn-body
  [response]
  (cond-> response
    (:body response)
    (update :body json/write-str)))

(defn wrap-json-body
  [handler]
  (fn [data]
    (handler
     (update-in data [:request :body]
                (fn [body]
                  (some-> body (json/read-str :key-fn keyword)))))))

(defn add-context
  [handler options]
  (fn [request]
    (handler (assoc options :request request))))
