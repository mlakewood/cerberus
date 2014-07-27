(ns raft.node)

(defrecord CommittedNodeState [current-term voted-for log])
(defrecord VolatileNodeState [commit-idx last-applied])
(defrecord VolatileLeaderNodeState [next-idx match-idx])
(defrecord LogEntry [term idx payload])
(defrecord LogLocationData [leaders-term leader-id leaders-commit-idx prev-log-term prev-log-idx])

;; The Log Protocol which is part of the persistent data
(defprotocol Log
    "Protocol for defining the log"
    (append-log [log log-location-data entry] "Append entry to the log"))

(extend-protocol Log
  clojure.lang.LazySeq
  (append-log [log log-location-data entry ];; {prev-log-idx :prev-log-idx  prev-log-term :prev-log-term} entry]
    (println "LazySeq")
    (if (and (matches-previous-entry log log-location-data) (>= (:term entry) (:term (last log))))
      (concat log [entry])
      nil)))

(extend-protocol Log
  nil
  (append-log [log log-location-data entry]
     (if (nil? log-location-data)
        (concat [] [entry])
        nil)))

(defn matches-previous-entry [log log-location-data]
  (let [last-known-term (:term (last log))
        last-known-idx (:idx (last log))
        leader-prev-term (:prev-log-term log-location-data)
        leader-prev-idx (:prev-log-idx log-location-data)]
    (if (and (= last-known-term leader-prev-term) (= last-known-idx leader-prev-idx))
      true
      false)))

(defn init-node []
  {:state :follower
   :committed-node-state (->CommittedNodeState nil nil nil)
   :volatile-node-state (->VolatileNodeState nil nil)
   :volatile-leader-node-state (->VolatileLeaderNodeState nil nil)})


(defn append-entries [follower-node log-location-data entries]
  ;; TODO[cw+mark, 2014/07/26]: simplifying this for now by assuming
  ;; we only have one entry but keeping the interface capable of
  ;; taking an array (for a later optimization).
  (if (not (= (count entries) 1))
    (throw (Exception. "Currently only support appending a single entry")))
  (let [entry (first entries)
        new-log (append-log (get-in follower-node [:committed-node-state :log]) log-location-data entry)
        new-committed-node-state (assoc (:committed-node-state follower-node) :log new-log)]
    (if (not (nil? new-log))
      {:success true :value (assoc follower-node :committed-node-state new-committed-node-state)}
      {:success false :value follower-node})))
