(ns app.pages.auth.form
  (:require [re-frame.core :as rf]
            [zenform.model :as zm]))

(def ^:const path [:form ::path])
(def ^:const schema
  {:type   :form
   :fields {:username         {:type :string}
            :password         {:type :string}
            :confirm-password {:type :string}}})

(rf/reg-event-fx
 ::init
 (fn []
   {:dispatch [:zf/init path schema]}))

(rf/reg-event-fx
 ::eval
 (fn [{db :db} [_ {:keys [success error]}]]
   (let [form (-> (get-in db path) zm/eval-form)]
     (if (seq (:errors form))
       {:dispatch [(:event error)   (:errors form)]}
       {:dispatch [(:event success) (:value  form)]}))))
