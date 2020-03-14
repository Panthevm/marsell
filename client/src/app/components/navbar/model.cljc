(ns app.components.navbar.model
  (:require [re-frame.core :as rf]))

(def ^:const links
  [{:title "Новинки" :href "#/home"}
   {:title "Кровати" :href "#/home/123"}
   {:title "Комоды" :href "#/"}])

(rf/reg-sub
 ::data
 (fn []
   {:nav links}))
