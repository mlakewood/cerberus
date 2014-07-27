(ns cerberus.raft.node-test
  (:require [clojure.test :refer :all]
            [raft.node :refer :all]))

(defn parse-response [response]
  (if (:success response)
    (:value response)
    (throw (Exception. "Response success == false"))))

(deftest test-node
 (testing "append entries to blank node"
    (let [node (init-node)
          log-location-data nil ;; (->LogLocationData nil nil nil nil nil)
          new-entries [(->LogEntry 0 0 :a)]
          expected-log [(->LogEntry 0 0 :a)]
          new-node (parse-response (append-entries node log-location-data new-entries))]  
      (is (= (get-in new-node [:committed-node-state :log]) expected-log))))

  (testing "append entries to non-empty log should fail to append due to prev log entry's index"
    (let [log-location-data-1 nil ;; (->LogLocationData nil nil nil nil nil)
          node-1 (parse-response (append-entries (init-node) log-location-data-1 [(->LogEntry 0 0 :a)]))
          log-location-data-2 (->LogLocationData nil nil nil 0 0)
          node-2 (parse-response (append-entries node-1 log-location-data-2 [(->LogEntry 0 1 :b)]))
          log-location-data-3 (->LogLocationData nil nil nil 0 0)
          new-entries [(->LogEntry 0 1 :c)]
          expected-log [(->LogEntry 0 0 :a) (->LogEntry 0 1 :b)]
          response (append-entries node-2 log-location-data-3 new-entries)]
      (is (= (:success response) false))
      (is (= (get-in response [:value :committed-node-state :log]) expected-log))))

  (testing "append entries to non-empty log should fail to append due to prev log entry's term"
    (let [log-location-data-1 nil ;; (->LogLocationData nil nil nil nil nil)
          node-1 (parse-response (append-entries (init-node) log-location-data-1 [(->LogEntry 0 0 :a)]))
          log-location-data-2 (->LogLocationData nil nil nil 0 0)
          node-2 (parse-response (append-entries node-1 log-location-data-2 [(->LogEntry 1 0 :b)]))
          log-location-data-3 (->LogLocationData nil nil nil 0 0)
          new-entries [(->LogEntry 1 0 :c)]
          expected-log [(->LogEntry 0 0 :a) (->LogEntry 1 0 :b)]
          response (append-entries node-2 log-location-data-3 new-entries)]
      (is (= (:success response) false))
      (is (= (get-in response [:value :committed-node-state :log]) expected-log))))

  (testing "append entries to non-empty log that should fail to append due to term non-monotonicity even though log-location-data is correct"
    (let [log-location-data-1 nil
          node-1 (parse-response (append-entries (init-node) log-location-data-1 [(->LogEntry 0 0 :a)]))
          log-location-data-2 (->LogLocationData nil nil nil 0 0)
          node-2 (parse-response (append-entries node-1 log-location-data-2 [(->LogEntry 2 0 :b)]))
          log-location-data-3 (->LogLocationData nil nil nil 2 0)
          new-entries [(->LogEntry 1 0 :c)]
          expected-log [(->LogEntry 0 0 :a) (->LogEntry 2 0 :b)]
          response (append-entries node-2 log-location-data-3 new-entries)]
      (is (= (:success response) false))
      (is (= (get-in response [:value :committed-node-state :log]) expected-log))))

  (testing "append entries to non-empty log successfully because of greater index"
    (let [log-location-data-1 nil
          node-1 (parse-response (append-entries (init-node) log-location-data-1 [(->LogEntry 0 0 :a)]))
          log-location-data-2 (->LogLocationData nil nil nil 0 0)
          node-2 (parse-response (append-entries node-1 log-location-data-2 [(->LogEntry 0 1 :b)]))
          log-location-data-3 (->LogLocationData nil nil nil 0 1)
          new-entries [(->LogEntry 1 0 :c)]
          expected-log [(->LogEntry 0 0 :a) (->LogEntry 0 1 :b) (->LogEntry 1 0 :c)]
          response (append-entries node-2 log-location-data-3 new-entries)]
      (is (= (:success response) true)
      (is (= (get-in response [:value :committed-node-state :log]) expected-log)))))
)
