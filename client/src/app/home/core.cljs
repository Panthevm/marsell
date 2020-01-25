(ns app.home.core
  (:require [app.pages      :as page]
            [app.home.model :as model]))

(page/reg-page
 model/index-page
 (fn []
   [:h1 "Главная страница"]))
