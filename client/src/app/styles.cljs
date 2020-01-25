(ns app.styles
  (:require [garden.core       :as garden]
            [garden.stylesheet :as stylesheet]))

(defn style [& styles]
  (garden/css (list styles)))

(def blue "#4980ff")
(def gray "#f2f5fc")

(def app
  (garden/css
   (list
    (stylesheet/at-media {:min-width "576px"}  [:.container {:max-width "540px"}])
    (stylesheet/at-media {:min-width "768px"}  [:.container {:max-width "720px"}])
    (stylesheet/at-media {:min-width "992px"}  [:.container {:max-width "960px"}])
    (stylesheet/at-media {:min-width "1200px"} [:.container {:max-width "1140px"}])
    [:body       {:margin           "0"
                  :font-family      "Helvetica"
                  :background-color gray
                  :padding-bottom   "1400px"}]
    [:.container {:width        "100%"
                  :margin-right "auto"
                  :margin-left  "auto"}]
    [:.content   {:padding-top "100px"}]

                                        ;#Position
    [:.fixed   {:position "fixed"}]
    [:.between {:display         "flex"
                :justify-content "space-between"}]
    [:.block   {:display "block"}])))
