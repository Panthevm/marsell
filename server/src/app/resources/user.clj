(ns app.resources.user
  (:require [json-schema.core :as schema]))

(def schema
  (schema/compile
   {:type       "object"
    :properties {:password     {:type "string"}
                 :email        {:type "string"}}
    :required [:email :password]}))

(def table
  {:table     :user
   :columns   {:id       {:type :serial :primary true :weighti 0}
               :resource {:type :jsonb}
               :tz       {:type :timestamptz}}
   :validator schema})
