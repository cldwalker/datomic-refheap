(ns refheap.models.users
  (:require [datomic-simple.core :as ds]
            [refheap.models.paste :as paste]))

(def model-namespace :user)
(def schema (ds/build-schema model-namespace
     [[:username :string]
      [:email :string]
      [:token :string]]))
(ds/create-model-fns model-namespace)

(defn get-user [user]
  (find-first {:username user}))

(defn find-by-email [email]
  (find-first {:email email}))

(defn get-user-by-id [id]
  (ds/find-id id))

(defn get-user-by-ref [paste-ref]
  (if-let [user (ds/expand-ref paste-ref)] (get-user-by-id (:id user))))

(defn user-pastes-for [user & [others]]
  (if-let [user (get-user user)]
    (paste/find-all (merge {:user (:id user)} others))
    '()))

(defn user-pastes [user page & [others]]
  (->>
    (user-pastes-for user others)
    (drop (* 10 (dec page)))
    (take 10)
    (sort-by :date #(compare %2 %1))))

(defn count-user-pastes [user & [others]]
  (count (user-pastes-for user others)))
