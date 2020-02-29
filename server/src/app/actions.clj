(ns app.actions
  (:require [clj-pg.honey  :as pg]
            [honeysql.core :as hsql]))

(defn -get [table {:keys [db params]}]
  (let [q (:ilike params)]
    {:staus 200
     :body  (pg/query db
                      (merge {:select [:*] :from [(:table table)]}
                             (when q
                               {:where [:ilike (hsql/raw "resource::text") (str \% q \%)]})))}))


(defn -post [table {:keys [db body]}]
  (let [insert   (pg/create db table {:resource body})
        response (pg/update db table
                            (update insert :resource assoc
                                    :resourceType  (-> table :table name)
                                    :id            (:id insert)))]
    {:status 201
     :body   response}))

(defn -delete [table {:keys [db params]}]
  (let [response (pg/delete db table (:id params))]
    {:status 200
     :body   response}))
