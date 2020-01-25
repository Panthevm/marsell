(ns app.helpers
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::expands
 (fn [db [_ key]]
   (get-in db [:expands key])))

(rf/reg-event-db
 ::expands
 (fn [db [_ key]]
   (update-in db [:expands key] not)))
