(ns fetchimagefsm.views2
  (:require
   [re-frame.core :as re-frame]
   [fetchimagefsm.subs :as subs]
   [statecharts.core :as fsm :refer [assign]] 
   [statecharts.integrations.re-frame :as fsm.rf]
  ; [ajax.core :as ajax]
    [ajax.protocols :refer [-body]]
   )
  )

(def image-url "https://YYpicsum.photos/200/300")

;n(def mymodule-path [(re-frame/path :imageFetcher)])

(declare fetch-image-service)


(re-frame/reg-event-fx
 ::imageFetcherinit
 (fn []
   (fsm/start fetch-image-service)          ;; (1)
   nil)
 )


(defn load-image []
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
     :on-failure #(fsm/send fetch-image-service {:type :fail-load :data %})

     :on-success #(fsm/send fetch-image-service {:type :success-load :data %})
     }
    }
   )
  )


(defn on-image-loaded [state {:keys [data]}]
  ; (assoc state :friends (:friends data))
  )

(defn on-image-load-failed [state {:keys [data]}]
  ; (assoc state :error (:status data))
  )


(def image-machine
  (fsm/machine
   {:id      :imageFetcher
    :initial :ready
    :states
    {:fetching     {:entry load-image
                   :on    {:success-load {:actions (assign on-image-loaded)
                                          :target  :success}
                           :fail-load    {:actions (assign on-image-load-failed)
                                          :target  :error
                                          }
                           }
                   }
     :success      {}
     :error {}
     }
    }
   )
  )


(defn ^:dev/after-load after-reload []
  (fsm/reload fetch-image-service image-machine)
  ) ;; (3)



(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1
      "Hello from " @name]
     ]))