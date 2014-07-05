(ns raft.zmq
  (:import [org.zeromq ZMQ])
  (:require (cheshire [core :as c])))

(def ctx (ZMQ/context 1))

(defn get-zctx []
    ctx)

(def zsocket (.socket (get-zctx) ZMQ/REP))

(def zbound (.bind zsocket "tcp://127.0.01:5555"))


(defn echo-server
  []
  (let [s (.socket ctx ZMQ/REP)]
    (.bind s "tcp://127.0.01:5555")
    (loop [msg (.recv s)]
      (.send s msg)
      (recur (.recv s)))))

(defn echo
  [msg]
  (let [s (.socket ctx ZMQ/REQ)]
    (.connect s "tcp://127.0.01:5555")
    (.send s msg)
    (println "Server replied:" (String. (.recv s)))
    (.close s)))