(ns app.core
  (:require [frames.server.core :as server]
            [app.middleware     :as middleware]
            [app.handler        :as handler])
  (:gen-class))

(defn -main [& args]
  (let [stack (-> #'handler/handler
                  middleware/allow-options
                  middleware/wrap-json-body
                  middleware/wrap-cors
                  middleware/wrap-edn-body)]
    (def server
      (server/run stack {:port 8080}))))

(comment
  (.close server))
