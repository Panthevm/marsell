(ns frames.server.request
  (:require [clojure.string :as str]
            [cheshire.core  :as ch]))

(def ^:const method-map
  {"GET"     :get
   "POST"    :post
   "DELETE"  :delete
   "OPTIONS" :options})

(defn- request-line [reader]
  (str/split (.readLine reader) #" " 3))

(defn- header-lines [reader]
  (let [headers (take-while not-empty (repeatedly #(.readLine reader)))]
    (reduce
     (fn [acc header]
       (let [[type value] (str/split header #": " 2)]
         (assoc acc type value)))
     {} headers)))

(defn- parse-uri [uri]
  (str/split uri #"\?" 2))

(defn- parse-body [headers reader]
  (letfn [(content-length [headers]
            (read-string (get headers "Content-Length")))
          (read-body [length]
            (let [buffer (char-array length)]
              (str (.read reader buffer 0 length))))]
    (when (contains? headers "Content-Length")
      (let [body (read-body (content-length headers))]
        (ch/parse-string body true)))))

(defn parse-request [reader]
  (let [[method uri version] (request-line reader)
        [uri query-string]   (parse-uri uri)
        headers              (header-lines reader)
        body                 (parse-body headers reader)]
    {:method        (get method-map method)
     :version       version
     :uri           uri
     :headers       headers
     :query-string  query-string
     :body          body}))
