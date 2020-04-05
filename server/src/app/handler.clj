(ns app.handler
  (:require [reitit.ring :as reitit]
            [app.actions :as action]
            [app.auth    :as auth]
            (app.resources
             [categories :as categories]
             [user       :as user])))

(def handler
  (reitit/ring-handler
   (reitit/router
    [["/categories" {:get    {:handler (partial action/-get    categories/table)}
                     :post   {:handler (partial action/-post   categories/table)}
                     :delete {:handler (partial action/-delete categories/table)}}]
     ["/auth"
      ["/login"        {:post {:handler auth/login}}]
      ["/registration" {:post {:handler auth/registration}}]]])
   (constantly {:status 404, :body "Not found"})))
