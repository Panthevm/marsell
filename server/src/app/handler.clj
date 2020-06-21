(ns app.handler
  (:require [frames.routing.core :as handler]
            [app.actions :as action]
            [app.auth    :as auth]
            (app.resources
             [categories :as categories]
             [user       :as user])))

(def ^:const routing
  {"categories" {:get    {:handler (partial action/-get    categories/table)}
                 :post   {:handler (partial action/-post   categories/table)}
                 :delete {:handler (partial action/-delete categories/table)}}
   "login" {:post {:handler auth/login}}
   "pin"   {:get  {:handler (fn [s] {:status 200 :body {:foo "pong"}})}}
   "post"  {:post {:handler (fn [s]
                              (prn s)
                              {:status 200 :body {:msg "post"}})}}
   "join"  {:post {:handler auth/join}}})

(def handler
  (handler/routing routing))
