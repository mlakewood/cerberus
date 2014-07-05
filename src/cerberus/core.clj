(ns cerberus.core
    (:use [carica.core])
    (:require [compojure.core :refer :all]
              [compojure.route :as route]
              [ring.adapter.jetty :as jetty]
              [clj-http.client :as client]
              [com.duelinmarkers.ring-request-logging :refer [wrap-request-logging]]
              ))


(def backends ((first (config :routes)) :backends))
; (def backends [{:remote-addr "127.0.0.1" :server-port 5000}
;                {:remote-addr "127.0.0.1" :server-port 5000}
;                {:remote-addr "127.0.0.1" :server-port 5000}
;                {:remote-addr "127.0.0.1" :server-port 5000}])
; 
(def backend-counter (atom (into [] (replicate (count backends) 0))))

(def request-save (atom {}))

(defn pick-backend []
    (let [end (first (apply min-key second (map-indexed vector @backend-counter)))]
        (swap! backend-counter update-in [end] inc)
        (int end)))

(defn match-url [route uri]
    (if (not (nil? (re-find (re-pattern route) uri)))
        true 
        false))

(defn fetch-backends [request]
    (filter #(match-url (% :route) (request :uri)) (config :routes)))

(defn rewrite [request route]
    (let [new-uri (clojure.string/replace (request :uri) 
                                          ((route :re-write) :match)
                                          ((route :re-write) :replace))]
    (assoc request :uri new-uri)))

(defn handler [request]
  (let [backends (first (fetch-backends request))
        route (first (config :routes))
        new-request (rewrite (merge request (get backends 0)) route)]
      (swap! request-save :request new-request)
      (client/request new-request)))

(defroutes app-routes
  (ANY "/*" [:as req] (handler req))
  (route/not-found "Page not found"))

(def app
  (-> (routes app-routes)
      (wrap-request-logging)))

(defn run
  [options]
  (let [options (merge {:port 8080 :join? true } options)]
    (jetty/run-jetty (var app-routes) options)))