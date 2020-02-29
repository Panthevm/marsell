(ns app.routes)

(def routes
  {:.       :app.pages.home.model/index
   "home"    {:.    :app.pages.home.model/index
              [:id] {:. :app.pages.home.model/show}}})
