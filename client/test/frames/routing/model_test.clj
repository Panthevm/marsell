(ns frames.routing.model-test
  (:require [frames.routing :as sut]
            [matcho.core    :as matcho]
            [clojure.test   :refer [deftest testing]]))

(def routes
  {:-      :foo
   "bar"  {:- :baz
           'bat {:- :quux
                 'plugh {:- :xyzzy
                         "qwe" {:- :uuex}}}}})

(deftest model
  (matcho/match (sut/match routes "#/")
                {:id     :foo
                 :params {:search nil}})

  (matcho/match (sut/match routes "#/bar")
                {:id     :baz
                 :params {:search nil}})

  (matcho/match (sut/match routes "#/bar?a=1&b=2")
                {:id     :baz
                 :params {:search {:a "1"
                                   :b "2"}}})

  (matcho/match (sut/match routes "#/bar/uuid?a=1&b=2")
                {:id     :quux
                 :params {:search {:a "1"
                                   :b "2"}
                          :bat    "uuid"}})

  (matcho/match (sut/match routes "#/bar/uuid/id?a=1&b=2")
                {:id     :xyzzy
                 :params {:search {:a "1"
                                   :b "2"}
                          :plugh  "id"
                          :bat    "uuid"}})

  (matcho/match (sut/match routes "#/bar/uuid/id/qwe?a=1&b=2")
                {:id     :uuex
                 :params {:search {:a "1"
                                   :b "2"}
                          :plugh  "id"
                          :bat    "uuid"}}))
