(ns app.rest
  (:require [immutant.web :as web]

            [app.migration  :as migration]
            [app.db         :as db]
            [app.middleware :as middleware]
            [app.handler    :as handler]))

(defn -main [& {:as args}]
  (let [db (db/connect)
        stack (-> #'handler/handler
                  (middleware/add-db db)
                  middleware/format-edn
                  middleware/parse-params
                  middleware/wrap-cors)]
    (migration/migration db)
    (web/run stack)))

:q
