(ns refheap.models.users
  (:require [datomic-simple.core :as ds]
            [refheap.models.paste :as paste]))

(def model-namespace :user)
(def schema (ds/build-schema model-namespace [[:username :string] [:email :string]]))

; TODO: detect empty user correctly
(defn expand-ref [m]
  (if (empty? m) nil (ds/localize-attr (ds/entity->map m))))

(defn create [attr]
  (ds/create model-namespace attr))

(defn get-user [user]
  (ds/local-find-first-by model-namespace {:username user}))

(defn find-by-email [email]
  (ds/local-find-first-by model-namespace {:email email}))

(defn get-user-by-id [id]
  (ds/local-find-id id))

(defn user-pastes-for [user & [others]]
  (if-let [user (get-user user)]
    (ds/local-find-by paste/model-namespace (merge {:user (:id user)} others))
    '()))

(defn user-pastes [user page & [others]]
  (->>
    (user-pastes-for user others)
    (drop (* 10 (dec page)))
    (take 10)
    (sort-by :date #(compare %2 %1))))

(defn count-user-pastes [user & [others]]
  (count (user-pastes-for user others)))
