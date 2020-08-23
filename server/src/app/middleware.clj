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
  (-> response
      (update :body json/write-str)))

(defn wrap-json-body
  [handler]
  (fn [data]
    (handler
     (update-in data [:request :body]
                (fn [body]
                  (some-> body (json/read-str :key-fn keyword)))))))

(defn allow-options
  [handler]
  (letfn [(allow [resource]
            {:status  200
             :headers {"Allow"
                       (->> (select-keys resource [:GET :POST :DELETE])
                            (map (comp name first))
                            (str/join ","))}})]
    (fn [data]
      (cond-> (handler data)
        (= :OPTIONS (-> data :request :method))
        (update :request allow)))))

(defn add-context
  [handler manifest]
  (fn [request]
    (handler {:request request :context manifest})))
