(ns app.rest
  (:require [frames.server.core :as server]
            [app.migration      :as migration]
            [app.db             :as db]
            [app.middleware     :as middleware]
            [app.handler        :as handler]))


(defn -main [& args]
  (let [db    (db/connect)
        stack (-> #'handler/handler
                  (middleware/add-db db)
                  middleware/wrap-cors)]
    (migration/migration db)
    (server/run stack {:port 8080})))
