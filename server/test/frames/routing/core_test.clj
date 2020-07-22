(ns frames.routing.core-test
  (:require [frames.routing.core :as sut]
            [matcho.core         :as matcho]
            [clojure.test        :refer [deftest testing]]))

(def routing
  (sut/routing
   {"resource" {:GET    {:handler (fn [request] (:method request))}
                :POST   {:handler (fn [request] (:method request))}
                :DELETE {:handler (fn [request] (:method request))}
                "child" {:GET     {:handler (fn [request] "child")}}
                'var    {:GET     {:handler (fn [request] (:params request))}
                         "nested" {:GET {:handler (fn [request] "nested")}
                                   'id  {:GET {:handler (fn [request] (:params request))}}}}}}
   {:not-found {:status 404 :body {:message "Resource not found"}}}))

(deftest routing-core-test

  (testing "methods"
    (matcho/match (routing {:uri "/resource" :method :GET})     :GET)
    (matcho/match (routing {:uri "/resource" :method :POST})    :POST)
    (matcho/match (routing {:uri "/resource" :method :DELETE})  :DELETE)
    (matcho/match (routing {:uri "/resource" :method :OPTIONS})
                  {:GET    {:handler fn?}
                   :POST   {:handler fn?}
                   :DELETE {:handler fn?}
                   "child" {:GET     {:handler fn?}}
                   'var    {:GET     {:handler fn?}
                            "nested" {:GET {:handler fn?}
                                      'id  {:GET {:handler fn?}}}}}))

  (testing "options"
    (testing "not-found"
      (matcho/match (routing {:uri "/not-found" :method :GET})
                    {:status 404 :body {:message "Resource not found"}})))

  (testing "params"
    (matcho/match (routing {:uri "/resource/child"    :method :GET})   "child")
    (matcho/match (routing {:uri "/resource/1"        :method :GET})   {:var "1"})
    (matcho/match (routing {:uri "/resource/1/nested" :method :GET})   "nested")
    (matcho/match (routing {:uri "/resource/1/nested/2" :method :GET}) {:var "1" :id "2"}))

  (testing "not-found"
    (matcho/match (routing {:uri "/foo"    :method :GET}) {:status 404 :body {:message "Resource not found"}})
    (matcho/match (routing {:uri "/foo/bar":method :GET}) {:status 404 :body {:message "Resource not found"}})))
