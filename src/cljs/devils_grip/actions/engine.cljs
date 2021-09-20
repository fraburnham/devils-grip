(ns devils-grip.actions.engine ; move this ns to a top level devils-grip.actions and drop the .engine from child ns
  (:require
   [devils-grip.actions.engine.draw :as draw]
   [devils-grip.actions.engine.fill-hole :as fill-hole]
   [devils-grip.actions.engine.from-talon :as from-talon]
   [devils-grip.actions.engine.merge-cells :as merge-cells]
   [devils-grip.actions.engine.start :as start]
   [devils-grip.actions.engine.swap-cells :as swap-cells]
   [devils-grip.rules :as rules]))

(defn deref-state-map [state-map]
  (->> (map (fn [[k v]] [k (deref v)]) state-map)
       (into {})))

;; new state is a plain map
;; old state is full of atoms
(defn update-state-map! [old-state new-state]
  (doseq [[k v] new-state]
    (swap! (old-state k) (constantly v))))

(defn selection-click! [action-state selection]
  (swap! action-state #(update % :selections conj selection)))

(defn action-click! [action-state type]
  (swap! action-state #(assoc % :type type)))

(defmulti advance
  (fn [{:keys [action-state stock talon board-state] :as state-map}]
    (:type action-state)))

(defmethod advance :draw
  [state-map]
  (draw/advance state-map))

(defmethod advance :fill-hole
  [state-map]
  (fill-hole/advance state-map))

(defmethod advance :from-talon
  [state-map]
  (from-talon/advance state-map))

(defmethod advance :merge-cells
  [state-map]
  (merge-cells/advance state-map))

(defmethod advance :start
  [state-map]
  (start/advance state-map))

(defmethod advance :swap-cells ; this is starting to look macro-able
  [state-map]
  (swap-cells/advance state-map))

(defmethod advance :default
  [state-map]
  (update state-map :action-state (constantly {})))

(defn advance! [{:keys [board-state] :as state-map}]
  (let [new-state-map (->> (deref-state-map state-map)
                           advance
                           rules/validate)]
    (update-state-map!
     state-map
     (if (:error new-state-map)
       (-> (deref-state-map state-map)
           (update-in [:action-state :error] (constantly (:error new-state-map)))
           (update :action-state {}))
       new-state-map))))
