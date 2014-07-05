(ns cerberus.raft.volatile-test
  (:require [clojure.test :refer :all]
            [raft.volatile :refer :all]))



(deftest test-leader
  (testing "Testing to become a leader"
    (let [end-state {:leader true :candidate false :follower false}]
        (become-leader)
        (is (= @state end-state)))))

(deftest test-candidate
  (testing "Testing to become a candidate"
    (let [end-state {:leader false :candidate true :follower false}]
        (become-candidate)
        (is (= @state end-state)))))

(deftest test-follower
  (testing "Testing to become a follower"
    (let [end-state {:leader false :candidate false :follower true}]
        (become-follower)
        (is (= @state end-state)))))


(deftest test-next-index
    (testing "testing setting the next index"
        (let [end-index {:id 1}]
            (set-next-index "id" 1)
            (is (= @next-index end-index)))))

(deftest test-match-index
    (testing "testing setting the next index"
        (let [end-index {:id 1}]
            (set-next-index "id" 1)
            (is (= @next-index end-index)))))