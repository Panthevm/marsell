(ns app.middleware
  (:require [ring.util.codec :as codec]
            [clojure.walk    :as walk]
            [cheshire.core :as json]))

(defn add-db [db handler]
  (fn [req]
    (handler (assoc req :db db))))

(defn format-edn [handler]
  (fn [req]
    (let [body* (when-let [b* (and (:body req) (not-empty (slurp (:body req))))]
                  (read-string b*))
          req* (-> req
                   (update :params merge body*)
                   (assoc :body body*))
          response (handler req*)]
      (cond-> response
        (:body response) (update :body json/generate-string)))))

(defn parse-params [handler]
  (fn [req]
    (if-let [qs (:query-string req)]
      (let [params
            (-> qs
                codec/form-decode
                walk/keywordize-keys)]
        (handler (update req :params merge params)))
      (handler req))))
