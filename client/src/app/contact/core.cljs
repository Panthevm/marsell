(ns app.contact.core
  (:require [app.pages      :as page]
            [app.contact.model :as model]))

(page/reg-page
 model/index-page
 (fn []
   [:div "Contact"]))
