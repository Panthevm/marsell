(ns app.home.model
  (:require [re-frame.core :as rf]))

(def index-page ::index)
(def show-page  ::show)

(rf/reg-event-fx
 index-page
 (fn [_ [_ hook]]
   (condp = hook
     :mount {:json/fetch {:uri     "/info"
                          :method  "get"
                          :success {:event ::success}}}
     nil)))


(rf/reg-event-fx
 show-page
 (fn [_ [_ & hook]]
   (prn "jook" hook)))

(rf/reg-event-db
 ::success
 (fn [db [_ data]]
   (assoc db :data data)))

(rf/reg-sub
 index-page
 (fn [db]
   {:data (:data db)}))
