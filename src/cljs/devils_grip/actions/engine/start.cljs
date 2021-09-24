(ns devils-grip.actions.engine.start
  (:require
   [devils-grip.components.cards :as cards]))

(defn board-setup [stock]
  (->> (take 24 stock)
       (partition 1)
       (mapv #(apply vector %))
       (partition 8)
       (mapv #(apply vector %))))

(defn advance [{:keys [action-state stock talon board-state] :as state-map}]
  "Setup the inital deck and board state"
  (let [stock (-> (cards/make-stock 2)
                  (cards/shuffle-stock))]
    {:talon []
     :board-state (board-setup stock)
     :stock (drop 24 stock)
     :action-state {}}))
