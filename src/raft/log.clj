(ns raft.log)


(defrecord LogEntry [index term payload])

(defrecord DummyLogEntry [index term payload])
(defrecord DummyLogEntry2 [foo bar baz])

(defn append-log [log entry prev]
    (if (every? identity (map = prev (last log)))
        (conj log entry)
        nil))
