(ns cerberus.core
    (:use compojure.core)
    (:require [lamina.core :as core]
              [aleph.http :as http]))

(defn handler [response-channel request]
  (core/enqueue response-channel
    {:status 200
     :headers {"content-type" "text/plain"}
     :body "Hello World"}))

(defroutes my-routes
  (GET "/", (wrap-aleph-handler handler))
  (route/not-found "Page not found"))

(defn -main [& args]
    (http/start-http-server (http/wrap-ring-handler my-routes) {:port 8080}))