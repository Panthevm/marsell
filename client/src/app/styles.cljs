(ns app.styles
  (:require [garden.core       :as garden]
            [garden.stylesheet :as stylesheet]))

(defn style [& styles]
  (garden/css (list styles)))

(defn media [opts & css]
  (stylesheet/at-media opts css))

(def gray "#f7f8f9")

(def app
  (garden/css
   (list
    [:body       {:margin           "0"
                  :font-family      "Helvetica"
                  :background-color gray
                  :padding-bottom   "1400px"}]
    [:.bg-white {:background-color "white"}])))
