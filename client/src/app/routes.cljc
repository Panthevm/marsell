(ns app.routes)

(def ^:const routes
  {:-     :app.pages.home.model/index
   "home" {:-    :app.pages.home.model/index
           [:id] {:- :app.pages.home.model/show}}})
