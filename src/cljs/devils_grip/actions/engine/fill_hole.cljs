(ns devils-grip.actions.engine.fill-hole)

(defn scan-cells [stock row] ; needs more refactor...
  (reduce
   (fn [{:keys [stock row] :as row-state} cell]
     (if (empty? cell)
       (-> (update row-state :row conj [(first stock)])
           (update :stock rest))
       (update row-state :row conj cell)))
   {:stock stock
    :row []}
   row))

(defn scan-row [{:keys [stock board-state] :as state-map} row]
  (let [{:keys [stock row]} (scan-cells stock row)]
    (-> (update state-map :board-state conj row)
        (update :stock (constantly stock)))))

(defn advance [{:keys [board-state] :as state-map}]
  (reduce scan-row (update state-map :board-state (constantly [])) board-state))
