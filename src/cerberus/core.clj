(ns cerberus.core
    (:use [lamina.core]
          [aleph.http]
          [carica.core])
    (:require [compojure.core :refer :all]
              [compojure.route :as route]
              [com.duelinmarkers.ring-request-logging :refer [wrap-request-logging]]
              ))

; (def backends (config :backends))
(def backends [{:remote-addr "127.0.0.1" :server-port 5000}
               {:remote-addr "127.0.0.1" :server-port 5000}
               {:remote-addr "127.0.0.1" :server-port 5000}
               {:remote-addr "127.0.0.1" :server-port 5000}])

(def backend-counter (atom (into [] (replicate (count backends) 0))))

(def request-save (atom {}))

(defn pick-backend []
    (let [end (first (apply min-key second (map-indexed vector @backend-counter)))]
        (swap! backend-counter update-in [end] inc)
        end))


(defn handler [response-channel request]
  (let [new-request (merge request (get backends pick-backend))]
      (swap! request-save :request new-request)
      (enqueue response-channel
        (http-request new-request))))

(defroutes app-routes
  (ANY "/*" [] (wrap-aleph-handler handler))
  (route/not-found "Page not found"))

(defn -main [& args] 
    (start-http-server 
        (wrap-ring-handler app-routes) {:port 8080}))
        ; (wrap-request-logging (wrap-ring-handler app-routes)) {:port 8080}))