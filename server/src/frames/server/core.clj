(ns frames.server.core
  (:require
   [clojure.java.io        :as io]
   [frames.server.request  :as request]
   [frames.server.response :as response])
  (:import
   [java.net ServerSocket SocketException]))

(defn- handle-client
  [handler socket]
  (with-open [reader (io/reader        socket)
              writer (io/output-stream socket)]
    (-> 
     (request/parse reader)
     (handler)
     (response/make writer))))

(defn run
  [handler options]
  (let [server-socket (ServerSocket. (:port options))]
    (future
      (loop []
        (let [socket (.accept server-socket)]
          (future
            (try (handle-client handler socket)
                 (catch SocketException e
                   (.close socket)))))
        (recur))
      (.close server-socket))
    server-socket))
