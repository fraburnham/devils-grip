(ns devils-grip.actions.engine.start
  (:require
   [devils-grip.cards :as cards]))

(defn board-setup! [stock]
  (->> (take 24 @stock)
       (partition 1)
       (mapv #(apply vector %))
       (partition 8)
       (mapv #(apply vector %))))

(defn reset-talon! [talon]
  (swap! talon (constantly [])))

(defn reset-stock! [stock]
  (swap! stock (fn [_]
                 (-> (cards/make-stock 2)
                     (cards/shuffle-stock)))))

(defn advance! [action-state stock talon board-state]
  "Setup the inital deck and board state"
  (reset-talon! talon)
  (reset-stock! stock)
  ;; keep board-state as a vector for `update-in` simplicity
  (swap! board-state (fn [_] (board-setup! stock)))
  (swap! stock (fn [stock] (drop 24 stock)))
  (swap! action-state (constantly {})))
