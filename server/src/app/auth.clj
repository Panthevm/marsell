(ns app.auth
  (:require [buddy.auth.backends.token :as token]
            [buddy.sign.jwt            :as jwt]
            [buddy.hashers             :as hashers]
            [app.resources.user        :as user]
            [app.actions               :as a]
            [clojure.string            :as str]
            [clj-pg.honey              :as pg]
            [cheshire.core             :as json]))

(def pkey "secret")

(def auth-backend
  (token/jws-backend {:secret pkey :options {:alg :es256}}))

(defn sign [privat-key payload]
  (jwt/sign payload pkey {:alg :es256}))


(defn unsign [public-key signed-data]
  (jwt/unsign signed-data public-key {:alg :es256}))

(defn -get [db email]
  (pg/query-first db
                  {:select [:*]
                   :from   [:user]
                   :where  ["@>" :resource
                            (json/generate-string {:email email})]}))

(defn login
  [{{:keys [email password]} :body db :db}]
  (let [user (:resource (-get db email))]
    (if (hashers/check password (:password user))
      (a/ok {:token (jwt/sign (dissoc user :password) pkey)})
      (a/bad-request))))

(defn join
  [request]
  (letfn [(encrypt [request]
            (update-in request [:body :password] hashers/encrypt))
          (clear [resource]
            (dissoc resource :password))]
    (let [insert (a/-post user/table (encrypt request))]
      (a/created
       {:token (jwt/sign {:user (-> insert :body :resource clear)}
                         pkey)}))))

(defn info
  [{{auth "authorization"} :headers}]
  (let [token (last (str/split auth #" "))
        info {:resource (jwt/unsign token pkey)}]
    (a/ok info)))
