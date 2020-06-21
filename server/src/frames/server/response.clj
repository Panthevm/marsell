(ns frames.server.response
  (:require [clojure.string :as str]
            [cheshire.core  :as ch])
  (:import  [java.io ByteArrayOutputStream]))

(def clrf "\r\n")

(def response-reasons {200 "OK"})

(defn- status-line [status]
  (str "HTTP/1.1 " status " " (get response-reasons status "Not Found")))

(defn- add-body-header [headers body]
  (cond-> headers
    body (assoc "Content-Length" (alength (.getBytes body)))))

(defn- build-headers [headers body]
  (let [extended-headers (add-body-header headers body)]
    (if (seq extended-headers)
      (reduce
       (fn [acc [k v]]
         (str acc k ": " v clrf))
       "" extended-headers))))

(defn build-response [{:keys [status headers body]}]
  (letfn [(append [out data]
            (when data
              (let [bs (.getBytes data)]
                (.write out bs 0 (alength bs)))))]
    (let [out    (ByteArrayOutputStream.)
          body   (some-> body ch/generate-string)]
      (append out (status-line status))
      (append out clrf)
      (append out (build-headers headers body))
      (append out clrf)
      (append out body)
      (.toByteArray out))))
