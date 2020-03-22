(ns app.pages.home.model
  (:require [re-frame.core :as rf]))

(def ^:const index-page ::index)
(def ^:const show-page  ::show)

(rf/reg-event-fx
 index-page
 (fn [_ [_ hook]]
   (condp = hook
     :mount {:json/fetch {:uri     "/categories"
                          :method  "get"
                          :success {:event ::success}}}
     nil)))

(rf/reg-event-db
 ::success
 (fn [db [_ data]]
   (assoc db :data data)))

(rf/reg-sub
 index-page
 (fn [db]
   {:data (:data db)}))
