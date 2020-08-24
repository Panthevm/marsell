(ns frames.xhr
  (:require [re-frame.core :as rf]
            [cljs.tools.reader.edn :as edn]))

(defn *json-fetch [{:keys [uri success error] :as opts}]
  (let [fetch-opts (-> (merge {:headers {"Content-Type" "application/edn"}} opts)
                       (dissoc :uri :success :error))
        fetch-opts (cond-> fetch-opts
                     (:body opts) (assoc :body (str (:body opts))))
        url        (str "http://localhost:8080" uri)]
    (->
     (js/fetch url (clj->js fetch-opts))
     (.then
      (fn [resp]
        (.then (.text resp)
               (fn [response]
                 (when-let [e (if (< (.-status resp) 299) success error)]
                   (rf/dispatch [(:event e) (edn/read-string response) (:params e)])))))))))

(defn json-fetch [opts]
  (if (vector? opts)
    (doseq [o opts] (*json-fetch o))
    (*json-fetch opts)))

(rf/reg-fx :json/fetch json-fetch)
