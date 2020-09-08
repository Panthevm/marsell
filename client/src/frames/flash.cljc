(ns frames.flash
  (:require [re-frame.core :as reframe]))

(reframe/reg-event-db
 ::remove
 (fn [db [_ {:keys [params]}]]
   (update db ::flash dissoc (:id params))))

(reframe/reg-event-fx
 ::flash
 (fn [{db :db} [_ {:keys [params]}]]
   (let [id (rand-int 1000)]
     {:db             (assoc-in db [::flash id] params)
      :dispatch-later [{:ms 3000 :dispatch [::remove {:params {:id id}}]}]})))

(reframe/reg-sub
 ::data
 (fn [db _]
   (::flash db)))
