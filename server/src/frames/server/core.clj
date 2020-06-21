(ns frames.server.core
  (:require [clojure.java.io :as io]
            [frames.server.request :as request]
            [frames.server.response :as response])
  (:import  [java.net ServerSocket]
            [java.util.concurrent Executors]))

(defn- read-request [handler reader]
  (->> reader
       (request/parse-request)
       (handler)
       (response/build-response)))

(defn- write-response [response writer]
  (.write writer response)
  (.flush writer))

(defn- exec-request [handler socket]
  (with-open [reader (io/reader        socket)
              writer (io/output-stream socket)]
    (-> handler
        (read-request   reader)
        (write-response writer))))

(defn run [handler options]
  (with-open [socket      (ServerSocket. (:port options))
              thread-pool (Executors/newFixedThreadPool 10)]
    (loop []
      (let [accept  (.accept socket)
            request (partial exec-request handler)]
        (.execute thread-pool #(request accept)))
      (recur))))
