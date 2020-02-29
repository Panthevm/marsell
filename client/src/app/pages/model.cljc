(ns app.pages.model
  (:require [re-frame.core :as rf]))

(defonce pages (atom {}))

(rf/reg-sub
 :pages/data
 (fn [db [_ pid]]
   (get db pid)))

(defn reg-page [key page]
  (swap! pages assoc key page))
