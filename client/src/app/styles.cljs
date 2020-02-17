(ns app.styles
  (:require [garden.core       :as garden]
            [garden.stylesheet :as stylesheet]))

(defn css [& styles]
  (garden/css (list styles)))

(defn media [opts & css]
  (stylesheet/at-media opts css))

(def color-1 "#e2f5fc")
(def color-2 "#999999")

(def app
  (garden/css
   (list
    (media {:min-width "576px"}  [:.container {:max-width "540px"}])
    (media {:min-width "768px"}  [:.container {:max-width "720px"}])
    (media {:min-width "992px"}  [:.container {:max-width "960px"}])
    (media {:min-width "1200px"} [:.container {:max-width "1140px"}])
    [:body       {:margin      "0"
                  :font-size   "1rem"
                  :font-family "Helvetica"}]
    [:.container {:width        "100%"
                  :margin-right "auto"
                  :margin-left  "auto"}]

    [:.muted {:color color-2}]

    [:.pointer {:cursor "pointer"}]
    [:.block   {:display "block"}]

    [:.between {:display         "flex"
                :justify-content "space-between"}]
    [:.center  {:align-items "center"
                :align-self   "center"}]

    [:.row {:display   "flex"
            :flex-wrap "wrap"}]
    [:.col {:flex-basis "0"
            :flex-grow  "1"
            :max-width  "100%"}])))
