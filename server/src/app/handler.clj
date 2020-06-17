(ns app.handler
  (:require [app.core.routing :as handler]
            [app.actions :as action]
            [app.auth    :as auth]
            (app.resources
             [categories :as categories]
             [user       :as user])))

(def handler
  (handler/routing
   {"categories" {:options {:handler (fn [e] {:status 200 :body nil})}
                  :get    {:handler (partial action/-get    categories/table)}
                  :post   {:handler (partial action/-post   categories/table)}
                  :delete {:handler (partial action/-delete categories/table)}}
    "login" {:options {:handler (fn [e] {:status 200 :body nil})}
             :post {:handler auth/login}}
    "join"  {:options {:handler (fn [e] {:status 200 :body nil})}
             :post {:handler auth/join}}}))
