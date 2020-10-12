(ns frames.server.core-test
  (:require [frames.server.core    :as sut]
            [frames.client.core    :as client]
            [clojure.java.io       :as io]
            [frames.server.request :as request]
            [matcho.core           :as matcho]
            [clojure.edn           :as edn]
            [clojure.test          :refer :all])
  (:import  [java.net Socket InetAddress]))

(deftest core 

  ;; 
  ;; Handler 
  ;; 
  ;; The `request` can contain the following keys:
  ;;   * :method       - Method type          | (:GET :POST :DELETE :OPTIONS ...)
  ;;   * :uri          - Relative URI         | "/relative-uri"
  ;;   * :version      - Version http request | "HTTP/1.1"
  ;;     :query-string - Query string         | "foo=bar&baz=zaz"
  ;;     :body         - Request body         | {:foo "bar"}

  (defn handler
    [request]
    {:status  200
     :headers {:Header "value"}
     :body    (str request)})

  ;;
  ;; Server
  ;;
  ;; Runs `handler` with the given `options`.
  ;; Options can contain the following keys:
  ;;   * :port - the port listening for requests
  ;;
  ;; 

  (with-open [server (sut/run handler {:port 3000})]

    (testing "GET"
      (def get-response
        (client/request
         {:method "GET"
          :url    "localhost/path?param=param-value"
          :port   3000}))

      (testing "status"
        (-> (:status get-response)
            (matcho/match "200")))

      (testing "headers"
        (-> (:headers get-response)
            (matcho/match
             {:Header         "value"
              :Content-Length "107"})))

      (testing "body"
        (-> (:body get-response)
            (matcho/match
             (str
              {:method       :GET
               :version      "HTTP/1.1"
               :uri          "path"
               :headers      {}
               :query-string "param=param-value"
               :body         nil})))))

    (testing "POST"
      (def post-response
        (client/request
         {:method  "POST"
          :url     "localhost/path?param-post=post"
          :port    3000
          :headers {:foo "bar"}
          :body    {:baz "zaz"}}))

      (testing "status"
        (-> (:status post-response)
            (matcho/match "200")))

      (testing "headers"
        (-> (:headers post-response)
            (matcho/match
             {:Header         "value"
              :Content-Length "151"})))

      (testing "body"
        (-> (:body post-response)
            (matcho/match
             (str
              {:method       :POST
               :version      "HTTP/1.1"
               :uri          "path"
               :headers      {:foo            "bar"
                              :Content-Length "12"}
               :query-string "param-post=post"
               :body         (str {:baz "zaz"})})))))))
