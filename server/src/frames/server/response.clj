(ns frames.server.response)

(def empty-line
  "\r\n")

(def response-reasons
  {200 "OK"
   201 "Created"
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
  (let [body (some-> body str)
        bytes-reponse
        (.getBytes
         (str (make-status status)
              empty-line
              (make-headers headers body)
              empty-line
              (str body)))]
    (.write writer bytes-reponse 0 (alength bytes-reponse))
    (.flush writer)))
