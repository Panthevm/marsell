(ns app.core.routing)

(defn cors [request node]
  (letfn [(allow-methods [node]
            (->> node
                 (reduce
                  (fn [acc [key value]]
                    (str acc (get {:get "GET" :post "POST" :delete "DELETE"} key) \,))
                  "")
                 drop-last (apply str)))]
    {:status  200
     :headers {"Access-Control-Allow-Origin"  "*"
               "Access-Control-Allow-Headers" "X-Requested-With,Content-Type,Cache-Control,Origin,Accept,Authorization"
               "Access-Control-Allow-Methods" (allow-methods node)}}))

(defn- match [routes uri]
  (loop [[current & other :as path] (re-seq #"[^/]+" uri)
         node               routes]
    (cond
      (get node current)               (recur other (get node current))
      (and (seq path) (get node :var)) (recur other (:var node))
      :else                            node)))

(defn routing [routes]
  (fn [request]
    (if-let [node (match routes (:uri request))]
      (let [method (:request-method request)]
        (if (= :options method)
          (cors request node)
          (let [handler (-> node method :handler)]
            (handler request))))
      2)))
