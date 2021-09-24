(ns devils-grip.actions.engine.from-talon
  (:require
   [devils-grip.cards :as cards]))

(defn talon->board [board-state action-state talon]
  (let [target (first (:selections action-state))]
    (update-in board-state target cards/merge-cards [(last talon)])))

(defmulti advance
  (fn [{:keys [action-state] :as state-map}]
    (count (:selections action-state))))

(defmethod advance 0
  [state-map]
  (update-in state-map [:action-state :help-text] (constantly "Select target cell")))

(defmethod advance 1
  [{:keys [action-state talon] :as state-map}]
  (-> (update state-map :board-state talon->board action-state talon)
      (update :talon drop-last)
      (update :action-state (constantly {}))))
