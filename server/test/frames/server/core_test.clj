(ns frames.server.core-test
  (:require [frames.server.core    :as sut]
            [clojure.java.io       :as io]
            [frames.server.request :as request]
            [matcho.core           :as matcho]
            [clojure.test          :refer :all])
  (:import  [java.net Socket InetAddress]))

(defn write
  [response writer]
  (.write writer response)
  (.flush writer))

(defn write-to
  [socket message]
  (write message (io/writer socket)))

(defn read-lines
  [socket]
  (line-seq (io/reader socket)))

(deftest server-core-test
  (with-open [server-socket (sut/run (fn [request]
                                       (prn request)
                                       {:status  200
                                        :headers {"Header" "Test"
                                                  "Header2" "Test2"}
                                        :body    (str request)})
                              {:port 3000})]

    (testing "GET"
      (with-open [socket (Socket. "localhost" (.getLocalPort server-socket))]
        (write-to socket
                  (str "GET /get?foo=bar&baz=zaz HTTP/1.1\n"
                       "Host: localhost\n"
                       "\n"))
        (matcho/match (read-lines socket)
                      ["HTTP/1.1 200 OK"
                       "Header: Test"
                       "Header2: Test2"
                       "Content-Length: 122"
                       ""
                       (str
                        {:method       :GET
                         :version      "HTTP/1.1"
                         :uri          "/get"
                         :headers      {:Host "localhost"}
                         :query-string "foo=bar&baz=zaz"
                         :body         nil})])))

    (testing "POST"
      (with-open [socket (Socket. "localhost" (.getLocalPort server-socket))]
        (write-to socket (str "POST /post HTTP/1.1\n"
                              "Host: localhost\n"
                              "Content-Length: 4\n"
                              "\n"
                              "body"))
        (matcho/match (read-lines socket)
                      ["HTTP/1.1 200 OK"
                       "Header: Test"
                       "Header2: Test2"
                       "Content-Length: 134"
                       ""
                       (str
                        {:method       :POST
                         :version      "HTTP/1.1"
                         :uri          "/post"
                         :headers      {:Host           "localhost"
                                        :Content-Length "4"}
                         :query-string nil
                         :body         "body"})])))

    (testing "OPTIONS"
      (with-open [socket (Socket. "localhost" (.getLocalPort server-socket))]
        (write-to socket (str "OPTIONS /options HTTP/1.1\n"
                              "Host: localhost\n"
                              "\n"))
        (matcho/match (read-lines socket)
                      ["HTTP/1.1 200 OK"
                       "Header: Test"
                       "Header2: Test2"
                       "Content-Length: 116"
                       ""
                       (str
                        {:method       :OPTIONS
                         :version      "HTTP/1.1"
                         :uri          "/options"
                         :headers      {:Host "localhost"}
                         :query-string nil
                         :body         nil})])))))

