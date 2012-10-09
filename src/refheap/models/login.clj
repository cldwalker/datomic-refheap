(ns refheap.models.login
  (:require [refheap.config :refer [config]]
            [clj-http.client :as http]
            [noir.session :as session]
            [cheshire.core :as json]
            [refheap.messages :refer [error]]
            [noir.request :refer [ring-request]]
            [refheap.models.users :as users]))

(defn create-user [email name]
  (let [name (.toLowerCase name)
        qmap {:email email
              :username name}
        name-count (count name)]
    ;; this stuff would be much cleaner with Validateur. MK.
    (cond
     (or (> 3 name-count) (< 15 name-count)) 
     (error "Username must be between 3 and 15 characters.")
     (not= name (first (re-seq #"\w+" name)))
     (error "Username cannot contain non-alphanumeric characters.")
     (users/get-user name)
     (error "Username already exists.")
     :else (let [user (users/create qmap)]
             (session/put! :user user)))))

(defn user-exists [email]
  (when-let [{:keys [username id]} (users/find-by-email email)]
    (session/put! :user {:email email
                         :username username
                         :id id})
    username))

(defn verify-host [hosts]
  (hosts (first (.split (get-in (ring-request) [:headers "host"]) ":"))))

(defn get-hosts []
  (if-let [hosts (System/getenv "HOSTS")]
    (set (.split hosts ","))
    (or (:hosts config) #{"localhost"})))

(defn verify-assertion [assertion]
  (let [verified (json/parse-string
                  (:body
                   (http/post "https://browserid.org/verify"
                              {:form-params
                               {:assertion assertion
                                :audience (verify-host (get-hosts))}}))
                  true)]
    (when (= "okay" (:status verified))
      verified)))
