(ns app.pages.auth.core
  (:require [frames.page                :as page]
            [re-frame.core              :as rf]
            [app.components.form.inputs :as inputs]
            [app.pages.auth.model       :as model]
            [app.pages.auth.form        :as form]))

(def buttons
  (letfn [(create []
            (rf/dispatch [::form/eval {:success {:event ::model/login}}]))]
    [:div.text-center
     [:button.btn.black.my {:on-click create} "Зарегистрироваться"]]))

(defn form []
  [:form
   [:h1.text-center.px "Регистрация"]
   [:label "Email"]
   [inputs/input form/path [:username]]
   [:label "Пароль"]
   [inputs/input form/path [:password]]])

(page/reg-page
 model/index-page
 (fn []
   [:div.container.center
    [:div
     [form]
     buttons]]))
