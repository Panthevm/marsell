(ns ^:figwheel-no-load app.dev
  (:require [app.core      :as core]
            [re-frisk.core :as frisk]))

(frisk/enable-re-frisk! {:width "400px" :height "500px"})
(core/mount)
