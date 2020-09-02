(ns app.components.navbar.core)

(defn component
  []
  [:nav.bg-purple-700.p-6.sticky.top-0.rounded-b-lg
   [:div.container.mx-auto.flex.items-center.flex-wrap
    [:div.flex.items-center.flex-shrink-0.text-white.mr-6
     [:span.font-semibold.text-xl.tracking-tight "Marsell"]]
    [:div.block.lg:hidden
     [:button.flex.items-center.px-3.py-2.border.rounded.text-white.border-white.hover:text-white.hover:border-white
      [:svg.fill-current.h-3.w-3
       {:viewbox "0 0 20 20"}
       [:title "Menu"]
       [:path {:d "M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z"}]]]]
    [:div.w-full.block.flex-grow.lg:flex.lg:items-center.lg:w-auto
     [:div.text-sm.lg:flex-grow
      [:a.block.mt-4.lg:inline-block.lg:mt-0.text-white.hover:text-white.mr-4
       {:href "#responsive-header"}
       "Docs"]
      [:a.block.mt-4.lg:inline-block.lg:mt-0.text-white.hover:text-white.mr-4
       {:href "#responsive-header"}
       "Examples"]
      [:a.block.mt-4.lg:inline-block.lg:mt-0.text-white.hover:text-white
       {:href "#responsive-header"}
       "Blog"]]
     [:div
      [:a.inline-block.text-sm.px-4.py-2.leading-none.border.rounded.text-white.border-white.mt-4.lg:mt-0
       {:href "#"}
       "Download"]]]]])
