(ns frames.routing-test
  (:require [frames.routing :as sut]
            [matcho.core    :as matcho]
            [re-frame.core  :as reframe]
            [re-frame.db    :as db]
            [clojure.test   :refer [deftest testing]]))

(def routes
  {:-      ::page-default
   "bar"  {:- ::page-1
           'bat {:- :quux
                 'plugh {:- :xyzzy
                         "qwe" {:- ::page-2}}}}})

(reframe/reg-event-fx
 ::page-default
 (fn [{db :db} [pid params]]
   {:db (assoc db pid params)}))

(reframe/reg-event-fx
 ::page-1
 (fn [{db :db} [pid params]]
   {:db (assoc db pid params)}))

(reframe/reg-event-fx
 ::page-2
 (fn [{db :db} [pid params]]
   {:db (assoc db pid params)}))

(deftest model
  (reset! db/app-db nil)
  (reframe/dispatch [::sut/init {:params {:routes routes}}])

  (reframe/dispatch [::sut/redirect {:params {:location "#/"}}])
  (matcho/match (::page-default @db/app-db )
                {:params {:id     ::page-default
                          :phase  :init
                          :params {:search nil}}})

  (reframe/dispatch [::sut/redirect {:params {:location "#/bar?init=foo"}}])
  (matcho/match (::page-default @db/app-db )
                {:params {:id     ::page-default
                          :phase  :deinit
                          :params {:search nil}}})
  (matcho/match (::page-1 @db/app-db)
                {:params {:id     ::page-1
                          :params {:search {:init "foo"}}
                          :phase  :init}})

  (reframe/dispatch [::sut/redirect {:params {:location "#/bar?init=fo"}}])
  (matcho/match (::page-1 @db/app-db)
                {:params {:id     ::page-1
                          :params {:search {:init "fo"}}
                          :phase  :params}})

  (reframe/dispatch [::sut/redirect {:params {:location "#/bar/123/456/qwe?init=fo"}}])
  (matcho/match (::page-1 @db/app-db)
                {:params {:id     ::page-1
                          :params {:search {:init "fo"}}
                          :phase  :deinit}})
  (matcho/match (::page-2 @db/app-db)
                {:params {:id     ::page-2
                          :phase  :init
                          :params {:bat    "123"
                                   :plugh  "456"
                                   :search {:init "fo"}},}})
  )
