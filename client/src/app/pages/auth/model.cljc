(ns app.pages.auth.model
  (:require [re-frame.core :as reframe]))

(def id ::page)

(reframe/reg-event-fx
 id
 (fn [_ [_ phase fragment-params :as ss]]
   (prn ss)
   {}))
