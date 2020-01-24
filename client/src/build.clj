(ns build
  (:require [cljs.build.api :as api]))

(def source-dir "src")

(def compiler-config
  {:output-to     "resources/public/js/app.js"
   :source-map    "resources/public/js/app.js.map"
   :output-dir    "resources/public/js/out"
   :optimizations :advanced
   :main          'app.build.prod
   :externs       []})

(defn -main []
  (api/build source-dir compiler-config))
