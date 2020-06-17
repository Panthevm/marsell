(ns app.middleware)

(defn add-db [handler db]
  (fn [req]
    (handler (assoc req :db db))))
