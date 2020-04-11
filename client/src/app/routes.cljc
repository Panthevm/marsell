(ns app.routes)

(def ^:const routes
  {:-      :app.pages.home.model/index
   "auth"  {:- :app.pages.auth.model/index}
   "home"  {:- :app.pages.home.model/index}})
