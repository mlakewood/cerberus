(ns raft.follower)

(def commit-index (atom 0N))

(def last-applied (atom 0N))

(defn increment-index []
    (swap! commit-index inc))

(defn increment-applied []
    (swap! last-applied inc))