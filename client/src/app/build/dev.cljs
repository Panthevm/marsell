(ns ^:figwheel-hooks app.dev
  (:require [app.core      :as core]
            [re-frisk.core :as frisk]))

(defn ^:after-load  dev []
  (frisk/enable-re-frisk! {:width "400px" :height "500px"})
  (core/mount))

(defonce load (dev))
