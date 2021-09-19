(ns devils-grip.actions.engine.fill-hole)

(defn check-cell [stock]
  (fn [cell]
    (if (empty? cell)
      (let [card (first @stock)]
        ;; this needs to account for the stock being empty (swap talon to stock)
        ;; the whole bit of keeping up w/ the stock seems like it should be
        ;; middleware around the `advance!` functionality
        ;; state validators could also live there
        ;; how to attach them? more macro stuff likely.
        (swap! stock rest)
        [card])
      cell)))

(defn scan-cells [stock]
  (fn [row]
    (doall
     (mapv (check-cell stock) row))))

(defn scan-rows [stock]
  (fn [board]
    (doall
     (mapv (scan-cells stock) board))))

(defn advance! [action-state stock _ board-state]
  (swap! board-state (scan-rows stock))
  (swap! action-state (constantly {})))
