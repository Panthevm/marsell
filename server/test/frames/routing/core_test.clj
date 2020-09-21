(ns frames.routing.core-test
  (:require [frames.routing.core :as sut]
            [matcho.core         :as matcho]
            [clojure.test        :refer [deftest testing]]))

(def routing
  (sut/routing
   {"resource" {:GET    {:handler (fn [{request :request}] (:method request))}
                :POST   {:handler (fn [{request :request}] (:method request))}
                :DELETE {:handler (fn [{request :request}] (:method request))}
                "child" {:GET     {:handler (fn [_] "child")}}
                'var    {:GET     {:handler (fn [{request :request}] (:params request))}
                         "nested" {:GET {:handler (fn [_] "nested")}
                                   'id  {:GET {:handler (fn [{request :request}] (:params request))}}}}}}
   {:not-found (fn [request]
                 {:status 404
                  :body {:message (str  "Resource " (:uri request) " not found")}})}))

(deftest core
  (testing "methods"
    (matcho/match (routing {:request {:uri "/resource" :method :GET}})     :GET)
    (matcho/match (routing {:request {:uri "/resource" :method :POST}})    :POST)
    (matcho/match (routing {:request {:uri "/resource" :method :DELETE}})  :DELETE))

  (testing "params"
    (matcho/match (routing {:request {:uri "/resource/child"    :method :GET}})   "child")
    (matcho/match (routing {:request {:uri "/resource/1"        :method :GET}})   {:var "1"})
    (matcho/match (routing {:request {:uri "/resource/1/nested" :method :GET}})   "nested")
    (matcho/match (routing {:request {:uri "/resource/1/nested/2" :method :GET}}) {:var "1" :id "2"}))

  (testing "not-found"
    (matcho/match (routing {:request {:uri "/foo"    :method :GET}}) {:status 404 :body {:message "Resource /foo not found"}})
    (matcho/match (routing {:request {:uri "/foo/bar":method :GET}}) {:status 404 :body {:message "Resource /foo/bar not found"}})))
