(ns devils-grip.actions.engine.merge-cells
  (:require
   [devils-grip.cards :as cards]))

(defn compare-cards [[suit-a rank-a] [suit-b rank-b]]
  (let [value-a (get cards/rank->value (name rank-a) (name rank-a))
        value-b (get cards/rank->value (name rank-b) (name rank-b))]
    (compare
     (js/parseInt value-a)
     (js/parseInt value-b))))

(defn merge-cards [new-cards existing-cards]
  (->> (concat existing-cards new-cards)
       (sort compare-cards)
       (into [])))

;; `source` and `target` should look like [row-num col-num]
(defn merge-cells [source target board]
  "Merge cells by moving cards from source to target"
  (-> (update-in board target (partial merge-cards (get-in board source)))
      (update-in source (constantly []))))

(defmulti advance!
  (fn [action-state board-state]
    (count (:selections @action-state))))

(defmethod advance! 0
  [action-state _]
  (swap! action-state #(assoc % :selections []))
  (swap! action-state #(assoc % :help-text "Select cell to take from")))

(defmethod advance! 1
  [action-state _]
  (swap! action-state #(assoc % :help-text "Select cell to place in")))

(defmethod advance! 2
  [action-state board-state]
  (let [[a b] (:selections @action-state)]
    (swap! board-state (partial merge-cells a b)))
  (swap! action-state (constantly {})))
