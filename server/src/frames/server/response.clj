(ns frames.server.response
  (:import [java.io ByteArrayOutputStream]))

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
  (let [headers (cond-> headers
                  body (assoc "Content-Length" (alength (.getBytes body))))]
    (reduce
     (fn [acc [k v]]
       (str acc k ": " v empty-line))
     "" headers)))

(defn make
  [{:keys [status headers body]} writer]
  (letfn [(append [out data]
            (when data
              (let [bs (.getBytes data)]
                (.write out bs 0 (alength bs)))))]
    (let [body (some-> body str)]
      (append writer (make-status status))
      (append writer empty-line)
      (append writer (make-headers headers body))
      (append writer empty-line)
      (append writer body)
      (.flush writer))))
