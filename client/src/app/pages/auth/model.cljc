(ns app.pages.auth.model
  (:require [re-frame.core :as reframe]))

(def authorization ::authorization)

(reframe/reg-event-fx
 authorization
 (fn [_ [_ phase fragment-params :as ss]]
   {}))

(def registration ::registration)

(reframe/reg-event-fx
 registration
 (fn [_ [_ phase fragment-params :as ss]]
   {}))

