(ns frames.server.core
  (:require [clojure.java.io        :as io]
            [clojure.tools.logging  :as logg]
            [clojure.core.async     :as async]
            [frames.server.request  :as request]
            [frames.server.response :as response])
  (:import  [java.net Socket ServerSocket SocketException]))

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
    (logg/info "Server started on port" (:port options))
    (future
      (loop []
        (let [socket (.accept server-socket)]
          (async/thread
            (try (handle-client handler socket)
                 (catch SocketException e
                   (logg/error e)
                   (.close socket)))))
        (recur))
      (.close server-socket))
    
    server-socket))
