(ns app.pages.login.core
  (:require [re-frame.core        :as rf]
            [app.pages.model      :as page]

            [app.pages.login.model :as model]))

(page/reg-page
 model/index-page
 (fn []
   [:div.container.center
    [:form
     [:h1.text-center.px "Авторизация"]
     [:label "Email"]
     [:input]
     [:label "Пароль"]
     [:input]
     [:div.text-center
      [:button.btn.black
       "Войти"]
      [:div
       [:a.muted {:href "#/login"}
        "Регистрация"]]
      ]]]))
