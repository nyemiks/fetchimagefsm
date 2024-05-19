(ns fetchimagefsm.machine2
   (:require 
    ; [statecharts.core :as fsm]
             [statecharts.impl :as impl])
  )



(def fsm-machine2 
   (impl/machine {
                 :id :imagefetcher2
                 :initial  :ready
                 :context {:image nil}
                 :entry   (fn [& _]
                           (.info js/console "just before ready state ")
                            )
                 :states {                          
                          :ready {:on {
                                       :button-clicked {
                                                        :target :fetching
                                                        :actions (fn [context & _]                                                                   
                                                                   (.info js/console "executing actions configured on :ready state ")
                                                                   (.info js/console "context: " context)
                                                                  )
                                                        }
                                       
                                       }
                                  }
                             :fetching {:entry (fn [context & _]
                                                                             (.info js/console "upon entering fetching state")
                                                                              (.info js/console "context: " context)
                                                                             )
                                        :on {
                                             :button-clicked-again {
                                                                    
                                                                    :target :ready
                                                                    :actions (fn [context & _]
                                                                         (.info js/console "executing actions configured :fetching state ") 
                                                                               (.info js/console "context: " context)
                                                                        )
                                                                    }
                                             
                                             }
                                        }
                          }
                 }
                )
  )


(defn image-comp2 []
  (let 
     [ 
       state (impl/initialize fsm-machine2)
        _    (.info js/console "initial state: " (:_state state))
        _     (.info js/console "before button click ..")
       state (impl/transition fsm-machine2 state :button-clicked)
        _     (.info js/console "after button click")
        _    (.info js/console "current state: " (:_state state))
        _     (.info js/console "before button click again ..")
       state (impl/transition fsm-machine2 state :button-clicked-again)
        _     (.info js/console "after button click again")
      ]   
    (.info js/console "current state: " (:_state state))
     (.info js/console "current state: " state)
    )
  )


(image-comp2)