(ns app.handler
  (:require [frames.routing.core :as handler]

            [app.action.person   :as person]))

(def routing
  {"ping"   {:GET  {:handler (fn [request]
                               {:status 200 :body (str request)})}}
   "person" {:POST {:handler person/create}}})

(def match-routing
  (handler/routing routing
                   {:not-found (fn [request]
                                 {:status 404
                                  :body {:message (str  "Resource " (:uri request) " not found")}})}))
