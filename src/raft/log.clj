(ns raft.log)


(defrecord LogEntry [index term payload])

(defn append-log [log entry prev]
    (if (every? identity (map = prev (last log)))
        (conj log entry)
        nil))