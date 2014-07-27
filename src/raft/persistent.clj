(ns raft.persistent
    (:require [clojure.java.io :as io]
              [clojure.edn :as edn])
    (:import [java.io PushbackReader]))



;; Define the operations on the Persistent Data

;; (defprotocol PersistentData
;;     "A protocol to wrap up the ther persistence of data"
;;     (read-stored-data [store file] "read in data from persistant store")
;;     (init [store id file] "intialise the persistant store")
;;     (update-id [store file id] "update id in persistance store")
;;     (increment-term [store file id] "increment the term")
;;     (update-voted-for [store file id voted-for] "update voted-for node")
;;     (update-log [store file id log-entry prev] "add an entry to the log"))


;; (extend-protocol PersistentData
;;     Persistent
;;     (read-stored-data [store file-name]
;;         (with-open [r (io/reader file-name)]
;;             (edn/read {:readers edn-readers} (PushbackReader. r))))

;;     (init [store id file]
;;         (let [exists (.exists (clojure.java.io/as-file file))]
;;             (if (not exists) (spit file (prn-str (->Persistent id 0 nil []))))))

;;     (update-id [store file id]
;;         (init store id file)
;;         (let [raft (read-stored-data store file)]
;;             (spit file (prn-str (assoc raft :id id)))
;;             (read-stored-data store file)))

;;     (increment-term [store file id]
;;         (init store id file)
;;         (let [raft (read-stored-data store file)]
;;             (spit file (prn-str (update-in raft [:term] inc)))
;;             (read-stored-data store file)))

;;     (update-voted-for [store file id voted-for]
;;         (init store id file)
;;         (let [raft (read-stored-data store file)]
;;             (spit file (prn-str (assoc raft :voted-for voted-for)))
;;             (read-stored-data store file)))

;;     (update-log [store file id log-entry prev]
;;         (init store id file)
;;         (let [raft (read-stored-data store file)
;;               log (:log raft)
;;               new-log (append-log log-entry prev log)]
;;             (if (not (nil? new-log))
;;                 (
;;                     (spit file (prn-str (assoc raft :log new-log)))
;;                     (read-stored-data store file))
;;                 nil)))
;; )

;; (defn persistent-factory []
;;    true)

