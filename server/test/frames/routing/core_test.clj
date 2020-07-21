(ns frames.routing.core-test
  (:require [frames.routing.core :as sut]
            [matcho.core         :as matcho]
            [clojure.test        :refer [deftest testing]]))

(def resource
  {:GET    {:handler (fn [request] {:status 200 :body (:method request)})}
   :POST   {:handler (fn [request] {:status 201 :body (:method request)})}
   :DELETE {:handler (fn [request] {:status 200 :body (:method request)})}
   "child" {:GET     {:handler (fn [request] {:status 200 :body "child"})}}
   :var    {:GET     {:handler (fn [request] {:status 200 :body "var-get"})}
            "nested" {:GET {:handler (fn [request] {:status 200 :body "nested"})}}}})

(def routing
  (sut/routing
   {"resource" resource}
   {:not-found {:status 404 :body {:message "Resource not found"}}}))

(deftest routing-core-test

  (testing "methods"
    (matcho/match (routing {:uri "/resource" :method :GET})     {:status 200 :body :GET})
    (matcho/match (routing {:uri "/resource" :method :POST})    {:status 201 :body :POST})
    (matcho/match (routing {:uri "/resource" :method :DELETE})  {:status 200 :body :DELETE})
    (matcho/match (routing {:uri "/resource" :method :OPTIONS}) resource))

  (testing "options"
    (testing "not-found"
      (matcho/match (routing {:uri "/not-found" :method :GET})
                    {:status 404 :body {:message "Resource not found"}})))

  (testing "params"
    (matcho/match (routing {:uri "/resource/child"    :method :GET}) {:status 200 :body "child"})
    (matcho/match (routing {:uri "/resource/1"        :method :GET}) {:status 200 :body "var-get"})
    (matcho/match (routing {:uri "/resource/1/nested" :method :GET}) {:status 200 :body "nested"})))
