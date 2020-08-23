(ns app.core
  (:require [frames.server.core :as server]
            [frames.pool.core   :as pool]
            [app.migration      :as migration]
            [app.middleware     :as middleware]
            [app.handler        :as handler]
            [app.manifest       :as manifest])
  (:gen-class))

(defn -main [& args]
  (let [db    (pool/db (-> manifest/manifest :database :datasource))
        stack (->
               middleware/wrap-edn-body
               middleware/wrap-cors
               handler/match-routing
               middleware/wrap-json-body
               (middleware/add-context
                {:db-connection db :manifest manifest/manifest}))]
    (migration/migration manifest/manifest db)
    (def server
      (server/run stack {:port 8080}))))

(comment
  (.close server))
