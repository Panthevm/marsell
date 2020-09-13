(ns app.events
  (:require [frames.form   :as form]
            [re-frame.core :as refreme]))

(defn event [action & [data]]
  (when action
    [(:event action) (merge (:params action) {:data data})]))

(refreme/reg-event-fx
 ::form-eval
 (fn [{db :db} [_ {:keys [params success]}]]
   (let [data (->> params :form-path (get-in db) form/export)]
     {:dispatch (event success data)})))


