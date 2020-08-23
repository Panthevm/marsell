(ns app.core
  (:require [frames.server.core :as server]
            [app.migration      :as migration]
            [app.middleware     :as middleware]
            [app.handler        :as handler]
            [app.manifest       :as manifest])
  (:gen-class))

(defn -main [& args]
  (let [stack (->
               middleware/wrap-edn-body
               middleware/wrap-cors
               handler/match-routing
               middleware/allow-options
               middleware/wrap-json-body
               (middleware/add-context manifest/manifest))]
    (migration/migration manifest/manifest)
    (def server
      (server/run stack {:port 8080}))))

(comment
  (.close server))
