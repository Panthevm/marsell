(ns app.handler
  (:require [frames.routing.core :as handler]))

(def routing
  {"pin"   {:GET  {:handler (fn [s] {:status 200 :body {:foo "pong"}})}}
   "post"  {:POST {:handler (fn [s]
                              {:status 200 :body {:msg "post"}})}}})

(def handler
  (handler/routing routing
                   {:not-found {:status 404 :body {:message "Resource not found"}}}))
