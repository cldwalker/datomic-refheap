(ns refheap.db
  (:require [datomic-simple.core :as ds]
            [datomic.api :as d]
            [refheap.models.users :as users]  
            [refheap.models.paste :as paste]))

(def uri "datomic:mem://refheap")

(defn start [options]
  (ds/start (merge options {:uri uri :schemas [paste/schema users/schema]})))
