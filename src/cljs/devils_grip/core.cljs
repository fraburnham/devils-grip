(ns devils-grip.core
  (:require
   [reagent.core :as reagent]
   [devils-grip.actions.engine :as actions]
   [devils-grip.board :as board] ; this and help should probably be in a `components` namespace for easier navigation (should put the page there, too)
   [devils-grip.cards :as cards]
   [devils-grip.help :as help]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(defonce stock
  (reagent/atom nil))

(defonce talon
  (reagent/atom []))

(defonce board-state
  (reagent/atom []))

(defonce action-state
  (reagent/atom {}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Page

(defn page [ratom]
  ;; divs may be nicer but tables are easy
  [:table
   [:tbody
    [:tr
     [:td {:id "title"} "Devil's Grip Solitare"]]
    [:tr
     [:td {:id "help"
           :style {:font-style "italic"}}
      (help/help action-state)]]
    [:tr
     [:td {:id "board"}
      (board/board
       (fn [] (actions/advance! action-state stock talon board-state))
       (partial actions/selection-click! action-state)
       board-state)]]
    [:tr
     [:td
      (board/stock stock)
      " " ; probably a better way to do this w/ styles
      (board/talon talon)
      " "
      (board/score stock talon)]]
    [:tr
     [:td {:id "actions"}
      [:button
       {:on-click
        (fn [_] ; some stuff can clearly be macros or fns
          (actions/action-click! action-state :draw)
          (actions/advance! action-state stock talon board-state))}
       "Draw"]
      [:button
       {:on-click
        (fn [_]
          (actions/action-click! action-state :from-talon)
          (actions/advance! action-state stock talon board-state))}
       "Place from talon"]
      [:button
       {:on-click
        (fn [_]
          (actions/action-click! action-state :fill-hole)
          (actions/advance! action-state stock talon board-state))}
       "Fill empty cells"]
      [:br]
      [:button
       {:on-click
        (fn [_]
          (actions/action-click! action-state :swap-cells)
          (actions/advance! action-state stock talon board-state))}
       "Swap cells"]
      [:button
       {:on-click
        (fn [_]
          (actions/action-click! action-state :merge-cells)
          (actions/advance! action-state stock talon board-state))}
       "Merge cells"]
      [:br]
      [:button
       {:on-click
        (fn [_]
          (actions/action-click! action-state :abort)
          (actions/advance! action-state stock talon board-state))}
       "Abort action"]
      [:br]
      [:button
       {:on-click
        (fn [_]
          (actions/action-click! action-state :start) ; will go to the default which clears state
          (actions/advance! action-state stock talon board-state))}
       "New game"]]]]])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn reload []
  (reagent/render [page] (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload))
