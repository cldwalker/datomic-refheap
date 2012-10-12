(ns refheap.server
  (:require [refheap.config :refer [config]]
            [noir.server :as server]
            [noir.util.middleware :refer [wrap-strip-trailing-slash wrap-canonical-host wrap-force-ssl]]
            [refheap.db :as db]))

(server/load-views "src/refheap/views/")
(server/add-middleware wrap-strip-trailing-slash)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (or (System/getenv "PORT") (str (config :port))))]
    (when (= mode :prod)
      (server/add-middleware wrap-canonical-host (System/getenv "CANONICAL_HOST"))
      (server/add-middleware wrap-force-ssl))
    (db/start {})
    ; TODO: :session-store for datomic
    (server/start port {:mode mode
                        :ns 'refheap})))

