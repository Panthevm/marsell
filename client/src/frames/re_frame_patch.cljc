(ns frames.re-frame-patch
  (:require [re-frame.events           :as events]
            [re-frame.subs             :as subs]
            [re-frame.interop          :as interop]
            [re-frame.fx               :as fx]
            [re-frame.cofx             :as cofx]
            [re-frame.std-interceptors :as std-interceptors]
            [re-frame.interceptor      :as interceptor]
            [re-frame.router           :as router]
            [re-frame.trace            :as trace :include-macros true]))

(defn dispatch      [{:keys [id params]}] (router/dispatch      [id params]))
(defn dispatch-sync [{:keys [id params]}] (router/dispatch-sync [id params]))
(defn subscribe     [{:keys [id params]}] (subs/subscribe       [id params]))

(fx/reg-fx
 ::dispatch
 (fn [value]
   (dispatch value)))

(fx/reg-fx
 ::dispatch-n
 (fn [value]
   (doseq [event (remove nil? value)]
     (dispatch event))))

(defn db-handler
  [handler-fn]
  (interceptor/->interceptor
    :id     :db-handler
    :before (fn db-handler-before
              [context]
              (let [new-context
                    (trace/with-trace
                      {:op-type   :event/handler
                       :operation (interceptor/get-coeffect context :event)}
                      (let [{db :db [id params] :event} (interceptor/get-coeffect context)]
                        (->> (handler-fn {:event-id id :db db :params params})
                             (interceptor/assoc-effect context :db))))]
                (trace/merge-trace!
                 {:tags {:effects   (interceptor/get-effect   new-context)
                         :coeffects (interceptor/get-coeffect context)}})
                new-context))))

(defn fx-handler
  [handler-fn]
  (interceptor/->interceptor
   :id     :fx-handler
   :before (fn fx-handler-before
             [context]
             (let [new-context
                   (trace/with-trace
                     {:op-type   :event/handler
                      :operation (interceptor/get-coeffect context :event)}
                     (let [{[id params] :event :as coeffects} (interceptor/get-coeffect context)]
                       (->> (handler-fn (assoc coeffects :id id :params params))
                            (assoc context :effects))))]
               (trace/merge-trace!
                {:tags {:effects   (interceptor/get-effect   new-context)
                        :coeffects (interceptor/get-coeffect context)}})
               new-context))))

(defn reg-event-db
  ([id handler]
   (reg-event-db id nil handler))
  ([id interceptors handler]
   (events/register id [cofx/inject-db fx/do-fx std-interceptors/inject-global-interceptors interceptors (db-handler handler)])))

(defn reg-event-fx
  ([id handler]
   (reg-event-fx id nil handler))
  ([id interceptors handler]
   (events/register id [cofx/inject-db fx/do-fx std-interceptors/inject-global-interceptors interceptors (fx-handler handler)])))
