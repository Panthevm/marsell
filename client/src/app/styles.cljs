(ns app.styles
  (:require [garden.core       :as garden]
            [garden.stylesheet :as stylesheet]))

(defn css [& styles]
  (garden/css (list styles)))

(defn media [opts & css]
  (stylesheet/at-media opts css))

(def color-1 "#e2f5fc")
(def color-2 "#999999")
(def color-3 "#f0f0f0")

(def font {:regular "GothamPro"
           :bold    "GothamPro-Bold"
           :light   "GothamPro-Light"})

(def typography
  (list
   [:h1 :b {:font-family (:bold font) :font-weight "900"}]
   [:h3    {:font-family (:bold font) :font-weight "400"}]
   [:small {:font-family (:light font)}]
   [:a     {:text-decoration "none"}]

   [:small {:font-size ".7rem"}]
   [:h1    {:font-size "42px"}]

   [:.muted {:color color-2}]))

(def positioning
  (list
   (media {:min-width "576px"}  [:.container {:max-width "540px"}])
   (media {:min-width "768px"}  [:.container {:max-width "720px"}])
   (media {:min-width "992px"}  [:.container {:max-width "960px"}])
   (media {:min-width "1200px"} [:.container {:max-width "1140px"}])
   [:.row {:display   "flex"
           ;:flex-wrap "wrap"
           }]
   [:.col {:flex-basis "0"
           :flex-grow  "1"
           :padding-right "1rem"
           :padding-left "1rem"
           :max-width  "100%"}]
   [:section {:padding-right "25px"}]
   [:.container {:width        "100%"
                 :margin-right "auto"
                 :margin-left  "auto"}]

   [:.space {:padding-right "5px"}]
   [:.pt {:padding-top "5px"}]
   [:.block   {:display "block"}]

   [:.between {:display         "flex"
               :justify-content "space-between"}]
   [:.self-center {:align-self "center"}]
   [:.end {:justify-content "end"}]
   [:.center  {:align-items "center"}]))

(def icons
  (list
   [:.small-icon {:height "10px"}]
   [:.icon {:height        "22px"
            :padding-right "16px"}]))

(def form
  (list
   [:.form
    [:input {:padding "5px 0px 5px 0px"
             }]
    [:.input-icon {:position "relative"}
     [:img
      {:position "absolute"
       :padding-top "5px"
       :height "22px"
       :top "0"
       :right "0"}]]
    [:input {:border "none"
             :width "100%"
             :border-bottom "1px solid #ccc"}
     [:&:focus {:border-bottom "1px solid #333"}]]]))

(def components
  (list
   [:.banner {:height     "13.75rem"
              :display    "block"
              :position   "relative"
              :background color-3}
    [:.caption {:position "absolute"}]]))

(def app
  (garden/css
   (concat
    typography
    positioning
    icons
    form
    components
    (list
     [:body       {:margin      "0"
                   :font-size   "1rem"
                   :font-family (:regular font)}]

     [:.pointer {:cursor "pointer"}]
     [:.border-bottom {:border-bottom "1px solid gray"}]


     ;;FORM
     [:button {}]
     ))))
