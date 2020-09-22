(ns app.middleware
  (:require [clojure.edn         :as edn]
            [frames.routing.core :as routing]))

(defn wrap-cors
  [handler]
  (fn [data]
    (-> (handler data)
        (assoc-in [:headers :Access-Control-Allow-Origin]  "*")
        (assoc-in [:headers :Access-Control-Allow-Methods] "GET,PUT,POST,DELETE")
        (assoc-in [:headers :Access-Control-Allow-Headers] "X-Requested-With,Content-Type,Cache-Control,Origin,Accept,Authorization"))))

(defn wrap-edn-body
  [handler]
  (fn [data]
    (handler
     (update-in data [:request :body]
                (fn [body]
                  (some-> body edn/read-string))))))

(defn add-context
  [handler options]
  (fn [request]
    (handler (assoc options :request request))))

(defn allow-options
  [handler]
  (fn [request]
    (cond
      (= :OPTIONS (:method request))
      {:status 200 :headers {:Allow "GET,POST,DELETE"}}
      :else (handler request))))
