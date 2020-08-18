(ns app.handler
  (:require [frames.routing.core :as handler]))

(def routing
  {"ping"  {:GET  {:handler (fn [request] {:status 200 :body request})}}
   "post"  {:POST {:handler (fn [s]
                              {:status 200 :body {:msg "post"}})}}})

(def handler
  (handler/routing routing
                   {:not-found {:status 404 :body {:message "Resource not found"}}}))
