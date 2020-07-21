(ns app.handler
  (:require [frames.routing.core :as handler]
            [app.actions :as action]
            [app.auth    :as auth]
            (app.resources
             [categories :as categories]
             [user       :as user])))

(def routing
  {"categories" {:GET    {:handler (partial action/-get    categories/table)}
                 :POST   {:handler (partial action/-post   categories/table)}
                 :DELETE {:handler (partial action/-delete categories/table)}}
   "login" {:POST {:handler auth/login}}
   "pin"   {:GET  {:handler (fn [s] {:status 200 :body {:foo "pong"}})}}
   "post"  {:POST {:handler (fn [s]
                              {:status 200 :body {:msg "post"}})}}
   "join"  {:POST {:handler auth/join}}})

(def handler
  (handler/routing routing
                   {:not-found {:status 404 :body {:message "Resource not found"}}}))
