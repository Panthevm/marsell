(ns app.components.infinite-scroll.core
  (:refer-clojure :exclude [list])
  (:require [reagent.core  :as reagent]
            [reagent.dom   :as dom]
            [goog.events   :as events]
            [re-frame.core :as reframe]

            [app.components.infinite-scroll.model :as model]
            [re-frame.core :as rf]))


                                        ;===========
                                        ;     1
                                        ;-----------
                                        ;     2
                                        ;-----------
                                        ;
                                        ;     3
                                        ;###########
                                        ;-----------
                                        ;     4
                                        ;-----------
                                        ;
                                        ;
                                        ;     5
                                        ;
                                        ;
                                        ;-----------
                                        ;     6
                                        ;-----------
                                        ;###########
                                        ;     7
                                        ;===========

(defn reached?
  [direction node]
  (case direction
    :top    (zero? (.-scrollTop node))
    :bottom (= (.-clientHeight node)
               (- (.-scrollHeight node)
                  (.-scrollTop node)))))

(defn item
  []
  (let [state (reagent/atom {:offset 0})])
  (letfn [(mount [this]
            (let [node   (dom/dom-node this)
                  args   (reagent/props this)
                  offset (.. node getBoundingClientRect -top)]
              (reframe/dispatch [::model/add-item {:focus (:focus args)
                                                   :item {:offset (- offset 400)}}])))

          (render [args [childrend idx]]
            (let [item*   (reframe/subscribe [::model/item {:focus (:focus args) :idx idx}])
                  offset* (reframe/subscribe [::model/get-scroll-position {:id (:focus args)}])]
              (fn []
                (let [offset @offset*
                      item   @item*]
                  [:div (str "Visible?" (<= offset (:offset item) (+ offset 300 50)))]))))]
    (reagent/create-class
     {:display-name        "person-info"
      :component-did-mount mount
      :reagent-render      render})))

(defn list
  []
  (letfn [(handler [node args]
            (reframe/dispatch [::model/set-scroll-position {:id (:id args)
                                                            :offset (.-scrollTop node)}])
            (prn "Top?"    (reached? :top node)
                 "Bottom?" (reached? :bottom node)))
          (mount [this]
            (let [node (dom/dom-node this)
                  args (reagent/props this)]
              (events/listen node "scroll" #(handler node args))))
          (unmount [this]
            (let [args (reagent/props this)]
              (reframe/dispatch [::model/deinitialize args])))
          (render [this] this)]
    (reagent/create-class
     {:component-did-mount    mount
      :component-will-unmount unmount
      :reagent-render         render})))
