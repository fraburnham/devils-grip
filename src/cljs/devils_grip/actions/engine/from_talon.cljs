(ns devils-grip.actions.engine.from-talon
  (:require
   [devils-grip.cards :as cards]))

;; this is kinda duplication, but I don't care to make a single fn that can handle this and the merge-cells need
;; probably refactor something in merge-cells ns
(defn merge-cards [existing-cards talon-card]
  (->> (conj existing-cards talon-card)
       (sort #(compare
               (js/parseInt (cards/rank->value (name (last %1))))
               (js/parseInt (cards/rank->value (name (last %2))))))
       (into [])))

(defn talon->board [action-state talon board-state]
  (let [target (first (:selections action-state))]
    (update-in board-state target merge-cards (last talon))))

(defmulti advance!
  (fn [action-state stock talon board-state]
    (count (:selections @action-state))))

(defmethod advance! 0
  [action-state _ _ _]
  (swap! action-state #(assoc % :help-text "Select target cell")))

(defmethod advance! 1
  [action-state _ talon board-state]
  (swap! board-state (partial talon->board @action-state @talon))
  (swap! talon drop-last)
  (swap! action-state (constantly {})))
