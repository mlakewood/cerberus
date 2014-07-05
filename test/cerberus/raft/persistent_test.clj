(ns cerberus.raft.persistent-test
  (:require [clojure.test :refer :all]
            [raft.persistent :refer :all])
  (:import [raft.persistent Persistent]))

; (deftest test-init
;   (testing "testing initialization of persistent store"
;     (let [id "node1"
;           log1 (->LogEntry 0 0 "foobar")
;           store (->Persistent id 0 nil [log1])
;           end-state (->Persistent "node1" 0 nil [log1])]
;         (init store id "raft-config.txt")
;         (is (= (read-stored-data ) end-state)))))


(deftest test-read-store-data
  (testing "testing reading from the raft-config"
    (let [log1 (->LogEntry 0 0 "foobar")
          store (->Persistent "node1" 0 nil [log1])
          end-state (->Persistent "node1" 0 nil [log1])]
        (read-stored-data "raft-config.txt")
        (is (= store end-state)))))

