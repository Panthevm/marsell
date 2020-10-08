(ns frames.server.request)

(defn- read-headers
  [reader]
  (let [headers (take-while not-empty (repeatedly #(re-seq #"[^: ]+" (.readLine reader))))]
    (reduce
     (fn [acc [type value]]
       (assoc acc (keyword type) value))
     {} headers)))

(defn- read-body
  [content-length reader]
  (let [length (Integer. content-length)
        buffer (char-array length)]
    (.read reader buffer 0 length)
    (String. buffer)))

(defn parse
  [reader]
  (let [[method uri version] (re-seq #"[^ ]+" (.readLine reader))
        [uri query-string]   (re-seq #"[^?]+" uri)
        headers              (read-headers reader)
        body                 (some-> (:Content-Length headers) (read-body reader))]
    {:method       (keyword method)
     :version      version
     :uri          uri
     :headers      headers
     :query-string query-string
     :body         body}))
