(ns app.components.breadcrumb.core)

(defn component
  []
  [:div.bg-purple-600.rounded-t-lg.p-2.mt-4
   [:ul.flex
    [:li.mr-6
     [:a.text-white {:href "#"} "Active"]]
    [:li.mr-6
     [:a.text-white {:href "#"} "Link"]]
    [:li.mr-6
     [:a.text-white {:href "#"} "Link"]]
    [:li.mr-6
     [:a.text-purple-400.cursor-not-allowed {:href "#"} "Disabled"]]]])

