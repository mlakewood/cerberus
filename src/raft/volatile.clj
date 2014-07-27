(ns raft.volatile)

;; volatile state on all servers
(def commit-index (atom 0N))

(def last-applied (atom 0N))

(defn increment-index []
    (swap! commit-index inc))

(defn increment-applied []
    (swap! last-applied inc))

(def state (atom {:leader false :candidate false :follower false}))

;;{:leader}
;;{:candidate}
;;{:follower}

(defn become-leader []
    (reset! state {:leader true :candidate false :follower false}))

(defn become-candidate []
    (reset! state {:leader false :candidate true :follower false}))

(defn become-follower []
    (reset! state {:leader false :candidate false :follower true}))

;; volatile state on leaders
(def next-index (atom {}))

(def match-index (atom {}))

(defn set-next-index [id index]
    (reset! next-index (assoc @next-index (keyword id) index)))

(defn set-match-index [id index]
    (reset! next-index (assoc @match-index (keyword id) index)))

