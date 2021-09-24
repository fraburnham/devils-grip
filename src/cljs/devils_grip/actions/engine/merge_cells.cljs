(ns devils-grip.actions.engine.merge-cells
  (:require
   [devils-grip.cards :as cards]))

;; `source` and `target` should look like [row-num col-num]
(defn merge-cells [board source target]
  "Merge cells by moving cards from source to target"
  (-> (update-in board target cards/merge-cards (get-in board source))
      (update-in source (constantly []))))

(defmulti advance
  (fn [{:keys [action-state]}]
    (count (:selections action-state))))

(defmethod advance 0
  [state-map]
  (-> (update-in state-map [:action-state :selections] (constantly []))
      (update-in [:action-state :help-text] (constantly "Select cell to take from"))))

(defmethod advance 1
  [state-map]
  (update-in state-map [:action-state :help-text] (constantly "Select cell to place in")))

(defmethod advance 2
  [{:keys [action-state] :as state-map}]
  (let [[a b] (:selections action-state)]
    (if (= a b)
      (update state-map :action-state (constantly {}))
      (-> (update state-map :board-state merge-cells a b)
          (update :action-state (constantly {}))))))
