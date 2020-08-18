(ns app.core
  (:require [frames.server.core :as server]
            [app.migration      :as migration]
            [app.middleware     :as middleware]
            [app.handler        :as handler]
            [app.manifest       :as manifest])
  (:gen-class))

(defn -main [& args]
  (let [stack (-> #'handler/handler
                  middleware/allow-options
                  (middleware/add-context manifest/manifest)
                  middleware/wrap-json-body
                  middleware/wrap-cors
                  middleware/wrap-edn-body)]
    (migration/migration manifest/manifest)
    (def server
      (server/run stack {:port 8080}))))

(comment
  (.close server))
