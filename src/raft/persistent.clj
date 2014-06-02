(ns raft.persistent
    (:require [clojure.java.io :as io]
              [clojure.edn :as edn])
    (:import [java.io PushbackReader]))


(defrecord Persistent [id term voted-for log])

(defrecord LogEntry [index term payload])

(defn append-log [log entry prev]
    (if (every? true? (map = prev (last log)))
        (conj log entry)
        nil))


(def edn-readers {'raft.persistent.Persistent map->Persistent
                  'raft.persistent.LogEntry map->LogEntry})

(defn read-persistent [file-name persistent]
    (with-open [r (io/reader file-name)]
        (reset! persistent (edn/read {:readers edn-readers} (PushbackReader. r)))))

(defn init-raft-persistent [id file]
    (let [exists (.exists (clojure.java.io/as-file file))]
        (if (not exists) (spit file (prn-str (->Persistent id 0 nil []))))))

(defn update-id [file id data]
    (init-raft-persistent id file)
    (let [raft (read-persistent file data)]
        (spit file (prn-str (assoc raft :id id)))
        (read-persistent file data)))

(defn increment-term [file id data]
    (init-raft-persistent id file)
    (let [raft (read-persistent file data)]
        (spit file (prn-str (update-in raft [:term] inc)))
        (read-persistent file data)))

(defn update-voted-for [file id voted-for data]
    (init-raft-persistent id file)
    (let [raft (read-persistent file data)]
        (spit file (prn-str (assoc raft :voted-for voted-for)))
        (read-persistent file data)))

(defn update-log [file id log-entry prev data]
    (init-raft-persistent id file)
    (let [raft (read-persistent file data)
          log (:log raft)
          new-log (append-log log log-entry prev)]
        (if (not (nil? new-log))
            (
                (spit file (prn-str (assoc raft :log new-log)))
                (read-persistent file data))
            nil)))