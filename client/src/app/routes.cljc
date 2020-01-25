(ns app.routes)

(def routes
  {:-       :app.home.model/index
   :home    {:- :app.home.model/index}
   :catalog {:- :app.contact.model/index}})
