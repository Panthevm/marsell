(ns frames.page
  (:require [re-frame.core :as rf]))

(defonce pages (atom {}))

(defn reg-page [key page]
  (swap! pages assoc key page))
