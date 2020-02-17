(ns app.components.navbar.model
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::data
 (fn []
   {:nav [{:title "Новинки" :href "#/"}
          {:title "Кровати" :href "#/"}
          {:title "Комоды" :href "#/"}]}))
