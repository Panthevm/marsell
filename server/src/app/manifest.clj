(ns app.manifest)

(def manifest
  {:resources
   {:person [[:id       "SERIAL"                         "PRIMARY KEY"]
             [:username "VARCHAR(32)"                    "NOT NULL"]
             [:password "VARCHAR(32)"                    "NOT NULL"]
             [:resource "VARCHAR(32)" "DEFAULT 'person'" "NOT NULL"]]}

   :database {:datasource {:connectionInitSql "select 1"
                           :autoCommit        true
                           :readOnly          false
                           :connectionTimeout 30000
                           :validationTimeout 5000
                           :idleTimeout       600000
                           :maxLifetime       1800000
                           :minimumIdle       10
                           :maximumPoolSize  10
                           :registerMbeans    false
                           :dataSourceClassName "org.postgresql.ds.PGSimpleDataSource"
                           :dataSource.Url     "jdbc:postgresql://localhost:5432/marsell?user=panthevm&stringtype=unspecified"}}})
