(ns app.core.routing)

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
  (let [method  (:request-method request)
        handler (-> resource method :handler)]
    (cond
      (= :options method) (option  resource)
      (fn? handler)       (handler request)
      (not handler)       (:not-found http))))

(defn routing
  [routes]
  (fn [request]
    (response request (match routes (:uri request)))))

;;[GET] curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/pin

;;[TIME] "curl 'http://localhost:8080/pin' -H 'Accept-Encoding: gzip, deflate, sdch' -H 'Accept-Language: en-US,en;q=0.8,ja;q=0.6' -H 'Upgrade-Insecure-Requests: 1' -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.86 Safari/537.36' -H 'Connection: keep-alive' --compressed -s -o /dev/null -w  "
