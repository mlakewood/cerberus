(ns raft.node)

(defrecord CommittedNodeState [current-term voted-for log])
(defrecord VolatileNodeState [commit-idx last-applied])
(defrecord VolatileLeaderNodeState [next-idx match-idx])
(defrecord LogEntry [term idx payload])
(defrecord LogLocationData [leaders-term leader-id leaders-commit-idx prev-log-idx prev-log-term])

;; The Log Protocol which is part of the persistent data
(defprotocol Log
    "Protocol for defining the log"
    (append-log [log log-location-data entries] "Append entries to the log"))

(extend-protocol Log
;;  clojure.lang.PersistentVector
  clojure.lang.LazySeq
  (append-log [log log-location-data entries ];; {prev-log-idx :prev-log-idx  prev-log-term :prev-log-term} entries]
    (if (empty? log)
      (if (nil? log-location-data) ;;(and (= 0 prev-log-idx) (= 0 prev-log-term))
        (concat log entries)
        nil)
      (if (and (= (:term (last log)) (:prev-log-term log-location-data)) (= (:index (last log)) (:prev-log-idx log-location-data)))
        (concat log entries)
        nil))))

(defn init-node []
  {:state :follower
   :committed-node-state (->CommittedNodeState nil nil [])
   :volatile-node-state (->VolatileNodeState nil nil)
   :volatile-leader-node-state (->VolatileLeaderNodeState nil nil)})


(defn append-entries [follower-node log-location-data entries]
  (let [new-log (append-log (get-in follower-node [:committed-node-state :log]) log-location-data entries)
        new-committed-node-state (assoc (:committed-node-state follower-node) :log new-log)]
    (if (not (nil? new-log))
      (assoc follower-node :committed-node-state new-committed-node-state)
      nil)))
