(ns app.routes)

(def routes
  {:-      :app.pages.auth.model/foo
   "authorization" {:- :app.pages.auth.model/authorization}
   "registration"  {:- :app.pages.auth.model/registration}})
