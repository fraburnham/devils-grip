(ns devils-grip.actions.engine
  (:require
   [devils-grip.actions.engine.draw :as draw]
   [devils-grip.actions.engine.fill-hole :as fill-hole]
   [devils-grip.actions.engine.from-talon :as from-talon]
   [devils-grip.actions.engine.merge-cells :as merge-cells]
   [devils-grip.actions.engine.start :as start]
   [devils-grip.actions.engine.swap-cells :as swap-cells]))

(defn selection-click! [action-state selection]
  (swap! action-state #(update % :selections conj selection)))

(defn action-click! [action-state type]
  (swap! action-state #(assoc % :type type)))

(defmulti advance!
  (fn [action-state stock talon board-state]
    (:type @action-state)))

(defmethod advance! :draw
  [action-state stock talon board-state]
  (draw/advance! action-state stock talon board-state))

(defmethod advance! :fill-hole
  [action-state stock talon board-state]
  (fill-hole/advance! action-state stock talon board-state))

(defmethod advance! :from-talon
  [action-state stock talon board-state]
  (from-talon/advance! action-state stock talon board-state))

(defmethod advance! :merge-cells
  [action-state _ _ board-state]
  (merge-cells/advance! action-state board-state))

(defmethod advance! :start
  [action-state stock talon board-state]
  (start/advance! action-state stock talon board-state))

(defmethod advance! :swap-cells ; this is starting to look macro-able
  [action-state _ _ board-state]
  (swap-cells/advance! action-state board-state))

(defmethod advance! :default
  [action-state _ _ _]
  (swap! action-state (constantly {})))
