(ns frames.server.core
  (:require [clojure.java.io :as io]
            [frames.server.request :as request]
            [frames.server.response :as response])
  (:import  [java.net ServerSocket Socket]
            [java.util.concurrent Executors]))

(defn- read-request
  [handler reader]
  (->> reader
       (request/parse)
       (handler)
       (response/make)))

(defn- write-response
  [response writer]
  (.write writer response)
  (.flush writer))

(defn- handle-client
  [handler server-socket]
  (with-open [socket (.accept server-socket)
              reader (io/reader        socket)
              writer (io/output-stream socket)]
    (-> handler
        (read-request   reader)
        (write-response writer))))

(defn run
  [handler options]
  (let [server-socket (ServerSocket. (:port options))]
    (future
      (loop []
        (handle-client handler server-socket)
        (recur))
      (.close server-socket))
    server-socket))
