(ns cerberus.proxy
    (:use [lamina.core]
          [aleph.http]))


(defn proxy [request]
    (http-request request))