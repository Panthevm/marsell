(ns ^:figwheel-no-load app.build.dev
  (:require [app.core      :as core]
            [re-frisk.core :as frisk]
            [devtools.core :as devtools]))

(devtools/install!)
(frisk/enable-re-frisk! {:width "400px" :height "500px" :x 0 :y 0})

(core/mount-root)
