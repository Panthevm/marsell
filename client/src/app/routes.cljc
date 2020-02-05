(ns app.routes)

(def routes
  {:.       :app.home.model/index
   "home"    {:.    :app.home.model/index
             [:id] {:. :app.home.model/show}}
   "catalog" {:. :app.contact.model/index}})
