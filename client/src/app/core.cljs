(ns ^:figwheel-hooks app.core
  (:require [reagent.core :as reagent]))

(defn current-page []
  [:h1 "@"])

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn ^:after-load re-render [] (mount-root))
