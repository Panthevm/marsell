(ns app.components.infinite-scroll.model
  (:require [re-frame.core  :as reframe]
            [re-frame.utils :as rutils]))

(reframe/reg-event-db
 ::deinitialize
 (fn [db [_ {:keys [id]}]]
   (rutils/dissoc-in db [::scroll id])))

(reframe/reg-event-db
 ::add-item
 (fn [db [_ {:keys [focus item]}]]
   (update-in db [::scroll focus :items]
              #(-> % vec (conj item)))))

(reframe/reg-event-db
 ::set-scroll-position
 (fn [db [_ {:keys [id offset]}]]
   (assoc-in db [::scroll id :offset] offset)))

(reframe/reg-sub
 ::get-scroll-position
 (fn [db [_ {:keys [id]}]]
   (get-in db [::scroll id :offset] 0)))

(reframe/reg-sub
 ::item
 (fn [db [_ {:keys [focus idx]}]]
   (get-in db [::scroll focus :items idx])))
