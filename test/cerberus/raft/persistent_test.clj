(ns cerberus.raft.persistent-test
  (:require [clojure.test :refer :all]
            [raft.node :refer :all]))

; (deftest test-init
;   (testing "testing initialization of persistent store"
;     (let [id "node1"
;           log1 (->LogEntry 0 0 "foobar")
;           store (->Persistent id 0 nil [log1])
;           end-state (->Persistent "node1" 0 nil [log1])]
;         (init store id "raft-config.txt")
;         (is (= (read-stored-data ) end-state)))))


;; (deftest test-read-store-data
;;   (testing "testing reading from the raft-config"
;;     (let [log1 (->LogEntry 0 0 "foobar")
;;           orig-store (->Persistent "node1" 0 nil [log1])
;;           read-store (->Persistent nil nil nil nil)]
;;       (write-store-date orig-store "raft-config.txt")
;;       (read-stored-data read-store "raft-config.txt")
;;       (is (= orig-store read-store)))))

(deftest test-node
 (testing "append entries to blank node"
    (let [node (init-node)
          log-location-data nil ;; (->LogLocationData nil nil nil nil nil)
          new-entries [(->LogEntry 0 0 :a)]
          expected-log [(->LogEntry 0 0 :a)]
          new-node (append-entries node log-location-data new-entries)]
      (is (= (get-in new-node [:committed-node-state :log]) expected-log))))

  ;; (testing "append entries to non-empty log that should fail to append due to earlier index"
  ;;   (let [log-location-data-1 nil ;; (->LogLocationData nil nil nil nil nil)
  ;;         node-1 (append-entries (init-node) log-location-data-1 [(->LogEntry 0 0 :a)])
  ;;         log-location-data-2 (->LogLocationData nil nil nil 0 0)
  ;;         node-2 (append-entries node-1 log-location-data-2 [(->LogEntry 0 1 :b)])
  ;;         new-entries [(->LogEntry 0 0 :c)]
  ;;         expected-log [(->LogEntry 0 0 :a) (->LogEntry 0 1 :b)]]
  ;;     (is (= (get-in node-2 [:committed-node-state :log]) expected-log))))

  ;; (testing "append entries to non-empty log that should fail to append due to earlier term"
  ;;   (let [node (append-entries (init-node) [(->LogEntry 0 1 :a)] nil)
  ;;         new-entries [(->LogEntry 0 0 :b)]
  ;;         expected-log [(->LogEntry 0 1 :a)]
  ;;         new-node (append-entries node new-entries log-location-data)]
  ;;     (is (= (get-in new-node [:committed-node-state :log]) expected-log))))

  ;; (testing "append entries to non-empty log successfully because of greater index"
  ;;   (let [node (append-entries (init-node) nil [(->LogEntry 0 0 :a)])
  ;;         new-entries [(->LogEntry 1 0 :b)]
  ;;         expected-log [(->LogEntry 0 0 :a) (->LogEntry 1 0 :b)]
  ;;         new-node (append-entries node nil new-entries)]
  ;;     (is (= (get-in new-node [:committed-node-state :log]) expected-log))))


)
