(ns app.handler
  (:require [reitit.ring :as reitit]
            [app.actions :as action]))

(def handler
  (reitit/ring-handler
   (reitit/router
    [["/info" {:get {:handler (fn [req] (action/-get :categories req))}}]])
   (constantly {:status 404, :body "not found"})))
