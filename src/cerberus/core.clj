(ns cerberus.core
    (:use [carica.core])
    (:require [compojure.core :refer :all]
              [compojure.route :as route]
              [clj-http.client :as client]
              [com.duelinmarkers.ring-request-logging :refer [wrap-request-logging]]
              ))

(def backends (config :backends))
; (def backends [{:remote-addr "127.0.0.1" :server-port 5000}
;                {:remote-addr "127.0.0.1" :server-port 5000}
;                {:remote-addr "127.0.0.1" :server-port 5000}
;                {:remote-addr "127.0.0.1" :server-port 5000}])

(def backend-counter (atom (into [] (replicate (count backends) 0))))

(def request-save (atom {}))

(defn pick-backend []
    (let [end (first (apply min-key second (map-indexed vector @backend-counter)))]
        (swap! backend-counter update-in [end] inc)
        (int end)))


(defn handler [request]
  (let [new-request (merge request (get backends 0))]
      (swap! request-save :request new-request)
      (client/request new-request)))

(defroutes app-routes
  (ANY "/*" [:as req] (handler req))
  (route/not-found "Page not found"))

(def app
  (-> (routes app-routes)
      (wrap-request-logging)))