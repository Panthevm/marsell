(ns frames.routing.core)

(def ^:const http {:not-found {:status 404 :body {:msg "Resource not found"}}
                   :ok        {:status 200}})

(defn option
  [resource]
  (letfn [(allow-methods [resource]
            (->> resource
                 (reduce
                  (fn [acc [key value]]
                    (str acc (get {:get "GET" :post "POST" :delete "DELETE"} key) \,))
                  "")
                 drop-last (apply str)))]
    (-> (:ok http)
        (assoc-in [:headers "Allow"] (allow-methods resource)))))

(defn match*
  [node [current & other :as path]]
  (cond (get node current)               (recur (get node current) other)
        (and (seq path) (get node :var)) (recur (:var node) other)
        :else                            node))

(defn match
  [router uri]
  (match* router (re-seq #"[^/]+" uri)))

(defn response [request resource]
  (let [method  (:method request)
        handler (-> resource method :handler)]
    (cond
      (= :options method) (option  resource)
      (fn? handler)       (handler request)
      (not handler)       (:not-found http))))

(defn routing
  [routes]
  (fn [request]
    (response request (match routes (:uri request)))))

;;[GET]  curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/pin
;;[POST] curl -X POST http://localhost:8080/post -d '{"variable": "value"}'

;; curl --output /dev/null --silent --write-out 'time_namelookup:  %{time_namelookup}s\n time_connect:  %{time_connect}s\n time_appconnect:  %{time_appconnect}s\n time_pretransfer:  %{time_pretransfer}s\n time_redirect:  %{time_redirect}s\n time_starttransfer:  %{time_starttransfer}s\n' http://localhost:8080/pin
