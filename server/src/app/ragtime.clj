(ns app.ragtime
  (:require [ragtime.jdbc :as jdbc])
  (:require [app.db       :as db])
  (:require [ragtime.repl :as repl]))

(def db-spec "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
(def database-url "jdbc:postgresql://localhost:5432/test_jdbc?user=test&password=1337&stringtype=unspecified")


(def config
  {:datastore  (jdbc/sql-database {:connection-uri database-url })
   :migrations (jdbc/load-resources "app/resources/migrations")})

;(repl/migrate config)

;(repl/rollback config)

