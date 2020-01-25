(ns app.components.navbar.core
  (:require [app.components.navbar.model :as model]))

(defn view []
  [:nav
   (map-indexed
    (fn [idx link] ^{:key idx}
      [:a link (:title link)])
    model/links)])
