(ns app.handler
  (:require [frames.routing.core :as handler]

            [app.action.person   :as person]))

(def routing
  {"ping"   {:GET  {:handler (fn [request] {:status 200 :body request})}}
   "person" {:POST {:handler person/create}}})

(defn match-routing
  [handler]
  (fn [data]
    (handler
     (handler/routing data routing
                      {:not-found {:status 404 :body {:message "Resource not found"}}}))))
