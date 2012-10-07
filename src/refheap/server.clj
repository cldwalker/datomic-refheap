(ns refheap.server
  (:require [refheap.config :refer [config]]
            [noir.server :as server]
            [noir.util.middleware :refer [wrap-strip-trailing-slash wrap-canonical-host wrap-force-ssl]]
            [refheap.db :as db]
            [monger.core :as mg]
            [monger.ring.session-store :refer [monger-store]]))

(let [uri (get (System/getenv) "MONGOLAB_URI" "mongodb://127.0.0.1/refheap_development")]
  (mg/connect-via-uri! uri))

(server/load-views "src/refheap/views/")
(server/add-middleware wrap-strip-trailing-slash)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (or (System/getenv "PORT") (str (config :port))))]
    (when (= mode :prod)
      (server/add-middleware wrap-canonical-host (System/getenv "CANONICAL_HOST"))
      (server/add-middleware wrap-force-ssl))
    (db/start {})
    (server/start port {:mode mode
                        :ns 'refheap
                        :session-store (monger-store "sessions")})))

