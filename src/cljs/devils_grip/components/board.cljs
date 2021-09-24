(ns devils-grip.components.board
  (:require
   [devils-grip.cards :as cards]))

(defn card [[suit rank] offset]
  (when (and suit rank)
    ^{:key {:suit suit :rank rank}}
    [:div {:class ["card" (cards/suit->color suit)]
           :style {:z-index offset
                   :top (str "-" (* offset 3.5) "em")}}
     [:div {:class "top-left"}
      (name rank) (cards/suit->symbol suit)]
     [:div {:class "bottom-right"}
      (name rank) (cards/suit->symbol suit)]]))

;; these should be using maps to reduce arity
(defn cell [advance-action! selection-click! row-num col-num cell-cards]
  ^{:key {:row row-num :cell col-num}}
  [:div {:class "cell"
        :id (str "row-" row-num "-col-" col-num)
        :on-click (fn [_]
                    (selection-click! [row-num col-num])
                    (advance-action!))}
   (map #(card %1 %2) cell-cards (range))])

(defn row [advance-action! selection-click! row-num row]
  ^{:key {:row row-num}}
  [:div {:id (str "row-" row-num)
         :class "row"}
   (map (fn [cell-data col-num]
          (cell advance-action! selection-click! row-num col-num cell-data))
        row
        (range))])

(defn board [advance-action! selection-click! {:keys [board-state]}]
  [:div {:class ["board"]}
   (map (fn [row-data row-num]
          (row advance-action! selection-click! row-num row-data))
        @board-state
        (range))])

(defn stock [{:keys [stock]}]
  [:span {:id "stock"}
   "Stock: " (count @stock)])

(defn talon [{:keys [talon]}]
  [:span {:id "talon"}
   "Talon: " (card (last @talon) 0)])

(defn score [{:keys [stock talon] :as state-map}]
  [:span {:id "score"}
   "Score: " (+ (count @stock) (count @talon))])
