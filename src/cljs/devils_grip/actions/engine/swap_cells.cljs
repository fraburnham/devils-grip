(ns devils-grip.actions.engine.swap-cells)

;; `a` and `b` should look like [row-num col-num]
(defn swap-cells [board a b] ; these fns that don't molest state should be pulled out into helpers (what do call that ns?)
  "Swap the contents of cells a and b"
  (let [a-data (get-in board a)]
    (-> (update-in board a (fn [_] (get-in board b)))
        (update-in b (fn [_] a-data)))))

(defmulti advance!
  (fn [action-state board-state]
    (count (:selections @action-state))))

(defmethod advance! 0
  [action-state _]
  (swap! action-state #(assoc % :selections []))
  (swap! action-state #(assoc % :help-text "Select first cell to swap")))

(defmethod advance! 1
  [action-state _]
  (swap! action-state #(assoc % :help-text "Select second cell to swap")))

(defmethod advance! 2
  [action-state board-state]
  (let [[a b] (:selections @action-state)]
    (swap! board-state #(swap-cells % a b)))
  (swap! action-state (constantly {})))
