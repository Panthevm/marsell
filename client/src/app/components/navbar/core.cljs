(ns app.components.navbar.core
  (:require [re-frame.core :as rf]
            [app.helpers :as h]))

(defn component []
  [:header.navbar.bg-white.p-2
   [:section.navbar-section
    [:a.btn.btn-link "Уведомления"]
    [:a.btn.btn-link "Уведомления"]
    [:a.btn.btn-link "Уведомления"]]
   [:section.navbar-center "MARSELL"]
   [:section.navbar-section
    [:button.btn.mr-2 "Корзина"]
    [:button.btn.mr-2 "Войти"]]])
