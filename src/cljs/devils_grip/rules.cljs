(ns devils-grip.rules
  (:require
   [devils-grip.components.cards :as cards]))

(defn valid-gap? [cards]
  ;; It seems like there should be a way to do this with a built-in and avoid recursion...
  (if (<= (count cards) 1)
    true
    (if (= 3 (- (cards/rank->value (second cards))
                (cards/rank->value (first cards))))
      (valid-gap? (rest cards))
      false)))

(defn same-suit? [cards]
  (let [suit (first (first cards))] ; don't really like knowing about the shape of cards/cells in here...
    (every? #(= suit (first %)) cards)))

(def validators
  [{:fail-message "Card rank must be 3 greater than existing cards"
    :fn valid-gap?}
   {:fail-message "Cards must be the same suit to be played together"
    :fn same-suit?}])

(defn validate-cell [cards]
  (->> (map #((:fn %) cards) validators)
       (every? true?)))

(defn validate-row [row]
  (->> (map validate-cell row)
       (every? true?)))

(defn validate-board [{:keys [board-state]}]
  (every? validate-row board-state)) ; oh derp this could be _just_ an every? since the answer is true/false

(defn validate [state-map]
  (if (validate-board state-map)
    state-map
    {:error "Inavlid move"}))
