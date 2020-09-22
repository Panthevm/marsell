(ns frames.server.request
  (:require [clojure.string        :as str]
            [clojure.tools.logging :as logg]))

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
        buffer (char-array  length)]
    (.read reader buffer 0 length)
    (String. buffer)))

(defn parse
  [reader]
  (let [[method uri version] (re-seq #"\w+"   (.readLine reader))
        [uri query-string]   (re-seq #"[^?]+" uri)
        headers              (read-headers reader)
        body                 (some-> (:Content-Length headers) (read-body reader))]
    (logg/info "Request:" method uri query-string headers body)
    {:method       (keyword method)
     :version      version
     :uri          uri
     :headers      headers
     :query-string query-string
     :body         body}))
