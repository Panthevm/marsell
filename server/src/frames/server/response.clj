(ns frames.server.response
  (:import [java.io ByteArrayOutputStream]))

(def ^:const empty-line
  "\r\n")

(def ^:const response-reasons
  {200 "OK"
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
  [{:keys [status headers body]}]
  (letfn [(append [out data]
            (when data
              (let [bs (.getBytes data)]
                (.write out bs 0 (alength bs)))))]
    (let [out  (ByteArrayOutputStream.)
          body (some-> body str)]
      (append out (make-status status))
      (append out empty-line)
      (append out (make-headers headers body))
      (append out empty-line)
      (append out body)
      (.toByteArray out))))
