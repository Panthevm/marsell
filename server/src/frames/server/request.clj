(ns frames.server.request
  (:require [clojure.string :as str]))

(defn- read-type
  [reader]
  (str/split (.readLine reader) #" " 3))

(defn- read-uri
  [uri]
  (str/split uri #"\?" 2))

(defn- read-header
  [reader]
  (let [headers (take-while not-empty (repeatedly #(.readLine reader)))]
    (reduce
     (fn [acc header]
       (let [[type value] (str/split header #": " 2)]
         (assoc acc type value)))
     {} headers)))

(defn- read-body
  [reader headers]
  (letfn [(read-body [content-length]
            (let [length (read-string content-length)
                  buffer (char-array  length)]
              (.read reader buffer 0 length)
              (String. buffer)))]
    (when-let [content-length (get headers "Content-Length")]
      (read-body content-length))))

(defn parse
  [reader]
  (let [[method uri version] (read-type   reader)
        [uri query-string]   (read-uri    uri)
        headers              (read-header reader)
        body                 (read-body   reader headers)]
    {:method       (keyword method)
     :version      version
     :uri          uri
     :headers      headers
     :query-string query-string
     :body         body}))
