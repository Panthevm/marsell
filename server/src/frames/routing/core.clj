(ns frames.routing.core)

(defn match*
  [current-node [current-path & other :as path] params]
  (letfn [(get-params-name [node]
            (ffirst (filter (comp symbol? key) node)))
          (set-params-node [params-name]
            (assoc params (keyword  params-name) current-path))]
    (let [next-node   (get current-node current-path)
          params-name (get-params-name current-node)]
      (cond
        next-node                    (recur next-node                      other params)
        (and (seq path) params-name) (recur (get current-node params-name) other (set-params-node params-name))
        :else                        [current-node params]))))

(defn match
  [router uri]
  (match* router (re-seq #"[^/]+" uri) {}))

(defn response
  [{request :request :as data} [resource params] options]
  (let [method    (:method request)
        handler   (-> resource method :handler)
        not-found (:not-found options)]
    (cond
      (= :OPTIONS method) resource
      (fn? handler)       (handler   (update data :request assoc :params params))
      (not handler)       (not-found data))))


(defn routing
  [{request :request :as data} routes options]
  (response data (match routes (:uri request)) options))

;;[GET]  curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/ping
;;[POST] curl -X POST http://localhost:8080/post -d '{"variable": "value"}'
;;[TIME] curl --output /dev/null --silent --write-out 'time_namelookup:  %{time_namelookup}s\n time_connect:  %{time_connect}s\n time_appconnect:  %{time_appconnect}s\n time_pretransfer:  %{time_pretransfer}s\n time_redirect:  %{time_redirect}s\n time_starttransfer:  %{time_starttransfer}s\n' http://localhost:8080/ping
