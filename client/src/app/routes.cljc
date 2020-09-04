(ns app.routes)

(def routes
  {:-      :app.pages.auth.model/foo
   "auth"  {:- :app.pages.auth.model/page}})
