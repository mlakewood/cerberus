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
