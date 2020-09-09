(ns frames.form-test
  (:require [frames.form  :as sut]

            [re-frame.core :as reframe]
            [re-frame.db   :as db]

            [matcho.core  :as matcho]
            [clojure.test :refer [deftest testing]]))

(def schema
  {:form-path   [:form ::schema1]
   :form-schema {:type  :form
                 :value {:birth-date {:type :string}
                         :period     {:type  :form
                                      :value {:start {:type :string}}}
                         :name       {:type  :collection
                                      :value {:type  :form
                                              :value {:given  {:type :collection}
                                                      :family {:type :string}}}}
                         :address    {:type  :collection
                                      :value {:type  :form
                                              :value {:text {:type :string}}}}}}
   :data        {:birth-date "1970-01-01"
                 :period     {:start "1999-09-09"}
                 :name       [{:given ["Name"  "Surname"]  :family "Family"}
                              {:given ["Name2" "Surname2"] :family "Family2"}]
                 :address    [{:text "address-1"}
                              {:text "address-2"}]}})


(deftest model
  (reset! db/app-db nil)
  (testing "events"
    (testing "eval"
      (testing "with data"
        (reframe/dispatch [::sut/init {:params schema}])
        (matcho/match
         (sut/export @(reframe/subscribe [::sut/form {:params schema}]) 
                   )
         (:data schema)))
      (testing "without data"
        (reframe/dispatch [::sut/init {:params (dissoc schema :data)}])
        (matcho/match
         (sut/export @(reframe/subscribe [::sut/form {:params schema}]))
         {:name [] :address [] :period {:start nil}})))

    (testing "set-value"
      (reframe/dispatch [::sut/init {:params schema}])
      (reframe/dispatch
       [::sut/set-value {:params (assoc schema
                                        :path  [:period :start]
                                        :value "2000-09-09")}])
      (reframe/dispatch
       [::sut/set-value {:params (assoc schema
                                        :path  [:birth-date]
                                        :value "1970-01-01")}])
      (reframe/dispatch
       [::sut/set-value {:params (assoc schema
                                        :path  [:name 0 :family]
                                        :value "family-2")}])
      (matcho/match
       (sut/export @(reframe/subscribe [::sut/form {:params schema}]) )
       (-> (:data schema)
           (assoc-in [:period :start]  "2000-09-09")
           (assoc-in [:name 0 :family] "family-2")
           (assoc-in [:birth-date]     "1970-01-01"))))

    (testing "collection"
      (testing "add"
        (reframe/dispatch [::sut/init {:params schema}])

        (reframe/dispatch
         [::sut/add-collection-item {:params (assoc schema
                                                    :path  [:name]
                                                    :value {:family "family3"
                                                            :given  ["name3" "surname3"]})}])

        (reframe/dispatch
         [::sut/add-collection-item {:params (assoc schema
                                                    :path  [:name 2 :given]
                                                    :value "surname-added")}])
        (matcho/match
         (sut/export @(reframe/subscribe [::sut/form {:params schema}]))
         (-> (:data schema)
             (update :name conj {:family "family3"
                                 :given  ["name3" "surname3" "surname-added"]}))))
      (testing "remove"
        (reframe/dispatch [::sut/init {:params schema}])

        (reframe/dispatch
         [::sut/remove-collection-item {:params (assoc schema
                                                       :path  [:name]
                                                       :index 0)}])
        (matcho/match
         (sut/export @(reframe/subscribe [::sut/form {:params schema}]))
         (-> (:data schema)
             (update :name rest)))))))
