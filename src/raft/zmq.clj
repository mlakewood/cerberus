(ns raft.zmq
  (:import [org.zeromq ZMQ])
  (:require (cheshire [core :as c])))

(defn get-zctx []
    (ZMQ/context 1))

(defn get-rep-socket [address]
   (let [sock (.socket (get-zctx) ZMQ/REP)]
     (.bind sock address)
     sock))

(defn get-req-socket [address]
   (let [sock (.socket (get-zctx) ZMQ/REQ)]
     (.connect sock address)
     sock))


(defn echo-server
  []
  (let [s (.socket get-zctx ZMQ/REP)]
    (.bind s "tcp://127.0.01:5555")
    (loop [msg (.recv s)]
      (println "got a message")
      (.send s msg)
      (recur (.recv s)))))

(defn echo
  [msg]
  (let [s (.socket get-zctx ZMQ/REQ)]
    (.connect s "tcp://127.0.01:5555")
    (.send s msg)
    (println "Server replied:" (String. (.recv s)))
    (.close s)))
