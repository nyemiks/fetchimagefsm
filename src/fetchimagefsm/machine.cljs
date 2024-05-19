(ns fetchimagefsm.machine
  (:require [statecharts.core :as fsm])
  )





(def fsm-machine 
   (fsm/machine {
                 :id :imagefetcher
                 :initial  :ready
                 :context {:image nil}
                 :entry   (fn [& _]
                           (.info js/console "just before ready state ")
                            )
                 :states {                          
                          :ready {:on {
                                       :button-clicked {
                                                        :target :fetching
                                                        :actions (fn [& _]
                                                                   (.info js/console "executing actions configured on :ready state ")
                                                                  )
                                                        }
                                       
                                       }
                                  }
                             :fetching {:on {
                                             :button-clicked-again {
                                                                    :target :ready
                                                                    :actions (fn [& _]
                                                                         (.info js/console "executing actions configured :fetching state ")
                                                                        )
                                                                    }
                                             
                                             }
                                        }
                          }
                 }
                )
  )



(defn image-comp []
  (let 
      [
        _    (.info js/console "init machine ...")
        service (fsm/service fsm-machine)
        _    (.info js/console "machine initialized ! ")
        _    (.info js/console "start machine ...")
        _   (fsm/start service)
        _    (.info js/console "machine started ! ")
        _   (.info js/console "initial state: " (fsm/value service))

      ;  s1 (fsm/initialize fsm-machine)
      ;  s2 (fsm/transition fsm-machine s1 {:type :button-clicked})
  ]
   ; (.info js/console "initial state: " (fsm/value service))
    (.info js/console "just before button click event  ... ")
    (fsm/send service :button-clicked)
    (.info js/console "state after button click: " (fsm/value service))
     (.info js/console "just before button clicked again event ... ")
     (fsm/send service :button-clicked-again)
    (.info js/console "state after button click again: " (fsm/value service))
    )
  )


(image-comp)