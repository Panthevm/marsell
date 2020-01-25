(ns app.home.model
  (:require [re-frame.core :as rf]))

(def index-page ::index)

(rf/reg-event-fx
 index-page
 (fn [{db :db} [_ phase params]]
   (prn phase)
   {:db db}))
