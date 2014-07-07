(ns raft.leader
  (:require [com.stuartsierra.component :as component]
            [raft.zmq :refer  [get-rep-socket get-req-socket]]
            [raft.persistent :refer [persistent-factory]]))


(defrecord Leader []
  component/Lifecycle
  (start [component]
    (println ";; Starting leader")
    (let [persistent (persistent-factory)]
      (assoc component :persistent persistent)))

  (stop [component]
    (println ";; stopping the leader")
    (dissoc component :persistent)
    component))






