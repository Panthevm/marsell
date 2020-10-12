(ns frames.client.core
  "TODO refactor"
  (:require
   [frames.server.request :as request]
   [frames.server.response :as response]
   [clojure.java.io       :as io])
  (:import
   [java.net Socket]))


(defn make
  [writer params]
  (let [body (some-> params :body str)
        bytes-reponse
        (.getBytes
         (str (:method params) " " (or (:path params) "/") " HTTP/1.1\n" 
              (response/make-headers (:headers params) body)
              "\r\n"
              body))]
    (.write writer bytes-reponse 0 (alength bytes-reponse))
    (.flush writer)))

(defn parse
  [reader]
  (let [[version status message]
        (re-seq #"[^ ]+" (.readLine reader))

        headers
        (request/read-headers reader)

        body
        (some-> (:Content-Length headers) (request/read-body reader))]
    {:status       status
     :headers      headers
     :body         body}))

(defn request
  [params]
  (let [[address path] (re-seq #"[^/]+" (:url params))]
    (with-open [socket (Socket. address (:port params))
                writer (io/output-stream socket)
                reader (io/reader socket)]
      (make writer (assoc params :path path))
      (parse reader))))
