(ns app.pages.auth.core
  (:require [frames.page   :as page]
            [re-frame.core :as reframe]

            [app.components.input.text   :as text-input]
            [app.components.buttons.dark :as button-dark]

            [app.pages.auth.model :as model]))

(defn registration
  []
  (let [data* (reframe/subscribe [model/registration])]
    (fn []
      (let [data @data*
            registration-button (-> data :buttons :registration)]
        [:div.flex.justify-center.mt-20
         [:div.w-full.max-w-xs
          [:h1.flex.justify-center.text-3xl.font-bold.text-white "Регистрация"]
          [:form.bg-purple-700.shadow-md.rounded.px-8.py-6.pb-8.mb-4.mt-3
           [:div.mb-4
            [text-input/component model/schema [:username] {:label "Логин"}]]
           [:div.mb-4
            [text-input/component model/schema [:password] {:label "Пароль"}]]
           [:div.mb-6
            [text-input/component model/schema [:password-confirm] {:label "Повторите пароль"}]]
           [:div.flex.items-center.justify-between
            [button-dark/component registration-button]]]
          [:p.text-center.text-gray-500.text-xs.text-sm
           [:span.pr-2 "Есть аккаунт?"]
           [:a.inline-block.align-baseline.font-bold.text-white
            {:href "#/authorization"}
            "Авторизируйтесь"]]]]))))

(defn authorization
  []
  [:div.flex.justify-center.mt-20
   [:div.w-full.max-w-xs
    [:h1.flex.justify-center.text-3xl.font-bold.text-white "Авторизация"]
    [:form.bg-purple-700.shadow-md.rounded.px-8.py-6.pb-8.mb-4.mt-3
     [:div.mb-4
      [text-input/component model/schema [:username] {:label "Логин"}]]
     [:div.mb-6
      [text-input/component model/schema [:password] {:label "Пароль"}]]
     [:div.flex.items-center.justify-between
      [button-dark/component {:label "Войти"}]]]
    [:p.text-center.text-gray-500.text-xs.text-sm
     [:span.pr-2 "Нет аккаунта?"]
     [:a.inline-block.align-baseline.font-bold.text-white
      {:href "#/registration"}
      "Регистрация"]]]])

(page/reg-page model/registration  registration)
(page/reg-page model/authorization authorization)
