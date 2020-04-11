(ns app.pages.auth.model
  (:require [re-frame.core       :as rf]
            [app.pages.auth.form :as form]))

(def ^:const index-page ::index)

(rf/reg-event-fx
 index-page
 (fn [_ [_ phase s]]
   (cond-> {}
     (#{:init :params} phase)
     (update :dispatch-n conj [::form/init]))))

(rf/reg-event-fx
 ::login
 (fn [_ [_ form]]
   {:json/fetch {:uri     "/login"
                 :method  "POST"
                 :body    form
                 :success {:event ::success-login}}}))

(rf/reg-event-fx
 ::success-login
 (fn [{db :db} [_ response]]
   {:db (assoc-in db [:config :token] (:token response))}))
