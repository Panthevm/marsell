(ns app.core
  (:gen-class)
  (:require [frames.server.core :as server]
            [frames.pool.core   :as pool]
            [app.migration      :as migration]
            [app.middleware     :as middleware]
            [app.handler        :as handler]
            [app.manifest       :as manifest]))

(defonce datasource
  (delay (pool/create-pool (-> manifest/manifest :database :datasource))))

(defn -main [& args]
  (let [stack (->
               handler/match-routing
               middleware/wrap-edn-body
               (middleware/add-context {:datasource datasource})
               middleware/allow-options
               middleware/wrap-cors)]
    (migration/migration manifest/manifest datasource)
    (def server
      (server/run stack {:port 8080}))))

(comment
  (.close server))
