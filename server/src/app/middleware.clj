(ns app.middleware
  (:require [cheshire.core       :as ch]
            [clojure.string      :as str]
            [frames.routing.core :as routing]))

(defn add-db
  [handler db]
  (fn [req]
    (handler (assoc req :db db))))

(defn wrap-json-body
  [handler]
  (fn [request]
    (handler
     (cond-> request
       (:body request) (update :body #(ch/parse-string % keyword))))))

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
        (update :body ch/generate-string))))

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
