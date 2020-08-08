(ns frames.server.core-test
  (:require [frames.server.core    :as sut]
            [clojure.java.io       :as io]
            [frames.server.request :as request]
            [matcho.core           :as matcho]
            [clojure.test          :refer :all])
  (:import  [java.net Socket]))

(defn write-to
  [socket message]
  (sut/write-response message (io/writer socket)))

(defn read-lines
  "Read all the lines currently loaded into the input stream of a socket."
  [socket]
  (line-seq (io/reader socket)))

(deftest server-core-test
  (with-open [server-socket (sut/run (fn [s] {:status 200
                                              :headers {"a" 1}
                                              :body "123"})
                              {:port 3000})
              socket        (Socket. "localhost" (.getLocalPort server-socket))]
    (write-to socket "GET /pin HTTP/1.1\nHost: localhost\n\n")
    (matcho/match (read-lines socket)
                  ["HTTP/1.1 200 OK" "a: 1" "Content-Length: 3" "" "123"] )))

