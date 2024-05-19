(ns fetchimagefsm.events
  (:require
   [re-frame.core :as re-frame]
   [fetchimagefsm.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::update-context
 (fn-traced [db [_ context]]
            (assoc db :imageFetcher context)
            )
 )
