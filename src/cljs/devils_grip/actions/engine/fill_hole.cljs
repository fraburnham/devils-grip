(ns devils-grip.actions.engine.fill-hole)

(defn fill-cell [{:keys [stock talon] :as row-state}]
  (if (empty? stock)
    (-> (update row-state :row conj [(first talon)])
        (update :stock (constantly (rest talon)))
        (update :talon (constantly [])))
    (-> (update row-state :row conj [(first stock)])
        (update :stock rest))))

(defn scan-cells [{:keys [stock talon] :as state-map} row] ; needs more refactor...
  (reduce
   (fn [{:keys [stock talon row] :as row-state} cell]
     (if (empty? cell)
       (fill-cell row-state)
       (update row-state :row conj cell)))
   {:stock stock
    :talon talon
    :row []}
   row))

(defn scan-row [{:keys [stock board-state] :as state-map} row]
  (let [{:keys [stock talon row]} (scan-cells state-map row)]
    (-> (update state-map :board-state conj row)
        (update :stock (constantly stock))
        (update :talon (constantly talon)))))

(defn advance [{:keys [board-state] :as state-map}]
  (reduce scan-row (update state-map :board-state (constantly [])) board-state))
