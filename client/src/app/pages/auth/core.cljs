(ns app.pages.auth.core
  (:require [frames.page :as page]

            [app.pages.auth.model :as model]))

(defn view
  []
  [:h1 "Auth"])

(page/reg-page model/id view)
