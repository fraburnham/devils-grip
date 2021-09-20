(ns devils-grip.actions.engine.swap-cells)

;; `a` and `b` should look like [row-num col-num]
(defn swap-cells [board a b]
  "Swap the contents of cells a and b"
  (let [a-data (get-in board a)]
    (-> (update-in board a (fn [_] (get-in board b)))
        (update-in b (fn [_] a-data)))))

(defmulti advance
  (fn [{:keys [action-state]}]
    (count (:selections action-state))))

(defmethod advance 0
  [state-map]
  (-> (update-in state-map [:action-state :selections] (constantly []))
      (update-in [:action-state :help-text] (constantly "Select first cell to swap"))))

(defmethod advance 1
  [state-map]
  (update-in state-map [:action-state :help-text] (constantly "Select second cell to swap")))

(defmethod advance 2
  [{:keys [action-state] :as state-map}]
  (let [[a b] (:selections action-state)]
    (-> (update state-map :board-state swap-cells a b)
        (update :action-state (constantly {})))))
