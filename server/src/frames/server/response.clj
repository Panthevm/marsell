(ns frames.server.response
  (:require [clojure.string :as str])
  (:import  [java.io ByteArrayOutputStream]))

(def empty-line "\r\n")

(def response-reasons {200 "OK"})

(defn- status-line [status]
  (str "HTTP/1.1 " status " " (get response-reasons status "Not Found")))

(defn- add-body-header [headers body]
  (cond-> headers
    body (assoc "Content-Length" (alength (.getBytes body)))))

(defn- build-headers [headers body]
  (let [extended-headers (add-body-header headers body)]
    (reduce
     (fn [acc [k v]]
       (str acc k ": " v empty-line))
     "" extended-headers)))

(defn build-response [{:keys [status headers body]}]
  (letfn [(append [out data]
            (when data
              (let [bs (.getBytes data)]
                (.write out bs 0 (alength bs)))))]
    (let [out (ByteArrayOutputStream.)]
      (append out (status-line status))
      (append out empty-line)
      (append out (build-headers headers body))
      (append out empty-line)
      (append out body)
      (.toByteArray out))))
