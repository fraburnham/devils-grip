(ns devils-grip.board
  (:require
   [devils-grip.cards :as cards]))

(defn card [[suit rank]]
  (when (and suit rank)
    ^{:key {:suit suit :rank rank}}
    [:span {:style {:color (cards/suit->color suit)}}
     (name rank) (cards/suit->symbol suit)]))

;; these should be using maps to reduce arity
(defn cell [advance-action! selection-click! row-num col-num cell-cards]
  ^{:key {:row row-num :cell col-num}}
  [:td {:style {:border "1px solid black"} ; style should be applied using classes to keep the code clearer
        :id (str "row-" row-num "-col-" col-num)
        :on-click (fn [_]
                    (selection-click! [row-num col-num])
                    (advance-action!))}
   (map #(card %) cell-cards)])

(defn row [advance-action! selection-click! row-num row]
  ^{:key {:row row-num}}
  [:tr {:id (str "row-" row-num)}
   (map (fn [cell-data col-num]
          (cell advance-action! selection-click! row-num col-num cell-data))
        row
        (range))])

(defn board [advance-action! selection-click! {:keys [board-state]}]
  [:table {:style {:background-color "green"}}
   [:tbody
    (map (fn [row-data row-num]
           (row advance-action! selection-click! row-num row-data))
         @board-state
         (range))]])

(defn stock [{:keys [stock]}]
  [:span {:id "stock"}
   "Stock: " (count @stock)])

(defn talon [{:keys [talon]}]
  [:span {:id "talon"}
   "Talon: " (card (last @talon))])

(defn score [{:keys [stock talon] :as state-map}]
  [:span {:id "score"}
   "Score: " (+ (count @stock) (count @talon))])
