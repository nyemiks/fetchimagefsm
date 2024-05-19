(ns fetchimagefsm.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [fetchimagefsm.events :as events]
   [fetchimagefsm.views :as views]
   [fetchimagefsm.config :as config]
   [day8.re-frame.http-fx]
   ;[fetchimagefsm.machine :as machine]
   ;[fetchimagefsm.machine :refer [s1x s1]]
  ; [fetchimagefsm.machine]
  ; [fetchimagefsm.machine2]
   ; [fetchimagefsm.machine3]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
