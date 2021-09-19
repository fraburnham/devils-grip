(ns devils-grip.actions.engine.draw
  (:require
      [devils-grip.actions.engine.start :as start]))

(defn advance! [action-state stock talon _]
  ;; this fn is grody. refactor to reduce repetition 
  (if (>= (count @stock) 3)
    (do
      (swap! talon #(->> (concat % (take 3 @stock))
                         (into [])))
      (swap! stock #(drop 3 %)))
    (do
      (swap! stock #(->> (concat % @talon)))
      (start/reset-talon! talon)
      (swap! talon #(->> (concat % (take 3 @stock))
                         (into [])))
      (swap! stock #(into [] (drop 3 %)))))
  (swap! action-state (constantly {})))

