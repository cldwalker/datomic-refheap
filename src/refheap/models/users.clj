(ns refheap.models.users
  (:refer-clojure :exclude [sort find])  
  (:require [monger.collection :as mc]
            [datomic-simple.core :as ds]
            [monger.query :refer [with-collection find sort limit skip]])
  (:import org.bson.types.ObjectId))

(def model-namespace :user)
(def schema (ds/build-schema model-namespace [[:username :string] [:email :string]]))

(defn create [attr]
  (ds/create model-namespace attr))

(defn get-user [user]
  (ds/local-find-first-by model-namespace {:username user}))
  ;(mc/find-one-as-map "users" {:username user}))

(defn find-by-email [email]
  (ds/local-find-first-by model-namespace {:email email}))

(defn get-user-by-id [id]
  nil)
  ;(mc/find-map-by-id "users" (ObjectId. id)))

(defn user-pastes [user page & [others]]
  (with-collection "pastes"
    (find (merge {:user (str (:_id (get-user user)))} others))
    (sort {:date -1})
    (limit 10)
    ;; TODO: switch to Monger's pagination support. MK.
    (skip (* 10 (dec page)))))

(defn count-user-pastes [user & [others]]
  (mc/count "pastes" (merge {:user (str (:_id (get-user user)))} others)))
