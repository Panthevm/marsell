(ns app.middleware
  (:require [clojure.data.json   :as json]
            [clojure.string      :as str]
            [frames.routing.core :as routing]))

(defn wrap-json-body
  [handler]
  (fn [request]
    (handler
     (cond-> request
       (:body request) (update :body #(json/read-str % keyword))))))

(defn wrap-cors
  [handler]
  (fn [request]
    (-> (handler request)
        (assoc-in [:headers "Access-Control-Allow-Origin"]  "*")
        (assoc-in [:headers "Access-Control-Allow-Methods"] "GET,PUT,POST,DELETE")
        (assoc-in [:headers "Access-Control-Allow-Headers"] "X-Requested-With,Content-Type,Cache-Control,Origin,Accept,Authorization"))))

(defn wrap-edn-body
  [handler]
  (fn [request]
    (-> (handler request)
        (update :body json/write-str))))

(defn allow-options
  [handler]
  (letfn [(allow [resource]
            {:status  200
             :headers {"Allow"
                       (->> (select-keys resource [:GET :POST :DELETE])
                            (map (comp name first))
                            (str/join ","))}})]
    (fn [request]
      (cond-> (handler request)
        (= :OPTIONS (:method request)) allow))))
