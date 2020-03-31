(ns app.pages.login.core
  (:require [frames.page      :as page]
            [app.pages.login.model :as model]))

(page/reg-page
 model/index-page
 (fn []
   [:div.container.center
    [:form
     [:h1.text-center.px "Авторизаця"]
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
