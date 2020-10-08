(ns frames.server.response)

(def empty-line
  "\r\n")

(def response-reasons
  {200 "OK"
   404 "Not Found"
   400 "Bad Request"})

(defn- make-status
  [status]
  (str "HTTP/1.1 " status " " (get response-reasons status "Not Found")))

(defn- make-headers
  [headers body]
  (let [headers
        (cond-> headers
          body (assoc :Content-Length (count body)))]
    (reduce
     (fn [acc [k v]]
       (str acc (name k) ": " v empty-line))
     "" headers)))

(defn make
  [{:keys [status headers body] :as response} writer]
  (.write writer
          (.getBytes (str (make-status status)
                          empty-line
                          (make-headers headers body)
                          empty-line
                          body)))
  (.flush writer))
