(ns frames.fetch
  (:require [re-frame.core :as rf]))

(defn json-fetch [{:keys [uri success error] :as opts}]
  (let [fetch-opts (-> (merge {:headers {"Content-Type" "application/json"}} opts)

                       (dissoc :uri :success :error))
        fetch-opts (cond-> fetch-opts
                     (:body opts) (assoc :body (.stringify js/JSON (clj->js (:body opts)))))
        url        (str "http://localhost:8080" uri)]
    (->
     (js/fetch url (clj->js fetch-opts))
     (.then
      (fn [resp]
        (.then (.json resp)
               (fn [doc]
                 (when-let [e (if (< (.-status resp) 299) success error)]
                   (rf/dispatch [(:event e) (js->clj doc :keywordize-keys true) (:params e)])))))))))

(rf/reg-fx :fetch json-fetch)
