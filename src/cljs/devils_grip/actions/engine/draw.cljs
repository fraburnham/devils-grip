(ns devils-grip.actions.engine.draw)

(defn draw [{:keys [stock talon] :as state-map}]
  (-> (update state-map :talon #(into [] (concat % (take 3 stock))))
      (update :stock #(drop 3 %))))

(defmulti advance
  (fn [{:keys [stock]}]
    (if (>= (count stock) 3)
      :stock-full
      :stock-empty)))

(defmethod advance :stock-full
  [state-map]
  (-> (draw state-map)
      (update :action-state {})))

(defmethod advance :stock-empty
  [{:keys [stock talon] :as state-map}]
  (-> (update state-map :stock #(->> (concat % talon)))
      (update :talon (constantly []))
      advance))
