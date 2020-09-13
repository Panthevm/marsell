(ns app.pages.auth.model
  (:require [frames.form   :as frames-form]
            [frames.flash  :as frames-flash]
            [app.events    :as events]
            [re-frame.core :as reframe]))

(def schema
  {:form-path   [:form ::path]
   :form-schema {:type  :form
                 :value {:username         {:type :string
                                            :validators [[:required {:message "Введите логин"}]]}
                         :password         {:type :string
                                            :validators [[:required {:message "Введите пароль"}]]}
                         :password-confirm {:type :string}}}})

(def authorization ::authorization)

(reframe/reg-event-fx
 authorization
 (fn [_ [_ {:keys [params]}]]
   (cond-> {}
     (= :init (:phase params))
     (update :dispatch-n conj
             [::frames-form/init {:params schema}]))))

(def registration ::registration)

(reframe/reg-event-fx
 registration
 (fn [_ [_ {:keys [params]}]]
   (cond-> {}
     (= :init (:phase params))
     (update :dispatch-n conj
             [::frames-form/init {:params schema}]))))

(reframe/reg-sub
 registration
 (fn []
   {:buttons
    {:registration {:label  "Зарегистрироваться"
                    :action
                    (fn []
                      (reframe/dispatch [::events/form-eval
                                         {:params  schema
                                          :success {:event ::create-account
                                                    :params {:success {:event ::success-create}}}}]))}}}))

(reframe/reg-event-fx
 ::create-account
 (fn [_ [_ {:keys [data params success]}]]
   {:dispatch [:js/fetch {:params {:uri     "/person"
                                   :method  "POST"
                                   :body    data
                                   :success success}}]}))

(reframe/reg-event-fx
 ::success-create
 (fn [_ [_ {:keys [data]}]]
   {:dispatch [::frames-flash/flash {:params {:status :success
                                              :message (str (:username data) ", вы успешно зарегистрировались")}}]}))

