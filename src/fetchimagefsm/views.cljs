(ns fetchimagefsm.views
  (:require
   [re-frame.core :as re-frame]
   [fetchimagefsm.subs :as subs]
   [fetchimagefsm.events :as events]
   [statecharts.core :as fsm :refer [assign]] 
   [statecharts.integrations.re-frame :as fsm.rf]
  ; [ajax.core :as ajax]
    [ajax.protocols :refer [-body]]
   )
  )

(def image-url "https://picsum.photos/200/300")

(def mymodule-path [(re-frame/path :imageFetcher)])




(defn load-image2 [state data]
   (.info js/console " -- load image 2 -- ")
  (.info js/console " context:  " state)
   (.info js/console " data:  " data)
  
  (re-frame/dispatch [::events/update-context state])  ;; my update for context
  ;(re-frame/dispatch [::fsm.rf/sync-state-update mymodule-path state :_state])
 ; (re-frame/dispatch [::fsm.rf/sync-state-update :imageFetcher state :_state])
  (.info js/console " updated context !  " )

  (.info js/console " call-fx ... ")
  ;(reframe/dispatch [:friends/fsm-event :success-load friends-data])
   (fsm.rf/call-fx
   {:http-xhrio
    {
     :uri image-url
     :method :get
     :response-format {
                        :content-type "image/png" 
                        :type :blob 
                        :description "PNG file" 
                        :read -body
                       }
     :on-failure [:imageFetcher/fsm-event :fail-load]
     :on-success [:imageFetcher/fsm-event :success-load]
     ;:on-failure (re-frame/dispatch [:imageFetcher/fsm-event :fail-load ])
     ; :on-success (re-frame/dispatch [:imageFetcher/fsm-event :success-load ])
     }
    }
   )
  )


(defn load-friends [state data]
  (.info js/console " load-friends:  " data)
   (.info js/console " context:  " state)
   (.info js/console " data:  " data)
  (re-frame/dispatch [:imageFetcher/fsm-event {:type :success-load} {:name "ike"}]))


(defn on-image-loaded [state {:keys [data]}]
   (.info js/console "-- on image loaded -- " )
   (.info js/console "context: " state)
 ; (assoc state :friends (:friends data))
    (.info js/console "data: " data)
  
    (re-frame/dispatch [::events/update-context state])  ;; my update for context
  )



(defn on-image-load-failed [state {:keys [data]}]
  (.info js/console " -- on image load failed -- " )
   (.info js/console "context: " state)
    (assoc state :error (:status data))
  )



(def fetch-image-machine
 
 (->  
  ( fsm/machine 
  {
     :id :imageFetcher,
     :initial :ready,
     :context {
       :image nil
     },
   :entry   (fn [& _]
                           (.info js/console "just before ready state ")
                            )
    :states {
       :ready {
          :on {
           :BUTTON_CLICKED :fetching
           }
         },
       :fetching {
                  :entry  load-image2
                                                                             
                  :on  {
                          :success-load {
                                          :actions (assign on-image-loaded)
                                          :target  :success
                                       }
                           :fail-load    {
                                            :actions on-image-load-failed
                                            :target  :error
                                          }
                        }
			      }, 
              :success {},
              :error {}
             },    
       :integrations {
                       :re-frame {
                                    :path mymodule-path               ;; (1)
                                    :initialize-event :imageFetcher/ready
                                    :transition-event :imageFetcher/fsm-event
                                 }
                    }
     }
    )

  (fsm.rf/integrate)  
  )
  
  )



(defn start-machine []
  (re-frame/dispatch [:imageFetcher/ready])
  )


(defn image-comp4 []
  (let 
     [ 
      _ (re-frame/dispatch [:imageFetcher/ready])
        state @(re-frame/subscribe [::subs/imageFetcher])
        name  @(re-frame/subscribe [::subs/name])
      ;  _ (.info js/console "current app state: " state)
     ;  _    (.info js/console "initial state: " (:_state state))
      ;  _     (.info js/console "before button click ..")
      ; state (fsm/transition fetch-image-machine state :BUTTON_CLICKED)
     ;   _     (.info js/console "after button click")
         ]   
    ;(.info js/console "current state: " (:_state state))
     (.info js/console "app name: " name)
    (.info js/console "current state: " state)
    )
  
  )


;(image-comp4)



(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1
      "Hello from " @name]
     ]))
