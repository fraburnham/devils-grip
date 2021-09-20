(ns devils-grip.core
  (:require
   [reagent.core :as reagent]
   [devils-grip.actions.engine :as actions]
   [devils-grip.board :as board] ; this and help should probably be in a `components` namespace for easier navigation (should put the page there, too)
   [devils-grip.cards :as cards]
   [devils-grip.help :as help]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(defonce stock ; pack all these directly on the state-map then defonce the state map
  (reagent/atom nil))

(defonce talon
  (reagent/atom []))

(defonce board-state
  (reagent/atom []))

(defonce action-state
  (reagent/atom {}))

(def state-map
  {:action-state action-state ; refactor the key names to not have `-state`
   :stock stock
   :talon talon
   :board-state board-state})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Page

(defn page [ratom]
  ;; divs may be nicer but tables are easy
  [:table
   [:tbody
    [:tr
     [:td {:id "title"} "Devil's Grip Solitare"]]
    [:tr
     [:td {:id "board"}
      (board/board
       (fn [] (actions/advance! state-map))
       (partial actions/selection-click! action-state)
       board-state)]
     [:td {:id "help"
           :style {:font-style "italic" :color "blue"}}
      (help/help action-state)]
     [:td {:id "error"
           :style {:font-style "italic" :color "red"}}
      (help/error action-state)]]
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
          (actions/advance! state-map))}
       "Draw"]
      [:button
       {:on-click
        (fn [_]
          (actions/action-click! action-state :from-talon)
          (actions/advance! state-map))}
       "Place from talon"]
      [:button
       {:on-click
        (fn [_]
          (actions/action-click! action-state :fill-hole)
          (actions/advance! state-map))}
       "Fill empty cells"]
      [:br]
      [:button
       {:on-click
        (fn [_]
          (actions/action-click! action-state :swap-cells)
          (actions/advance! state-map))}
       "Swap cells"]
      [:button
       {:on-click
        (fn [_]
          (actions/action-click! action-state :merge-cells)
          (actions/advance! state-map))}
       "Merge cells"]
      [:br]
      [:button
       {:on-click
        (fn [_]
          (actions/action-click! action-state :abort)
          (actions/advance! state-map))}
       "Abort action"]
      [:br]
      [:button
       {:on-click
        (fn [_]
          (actions/action-click! action-state :start) ; will go to the default which clears state
          (actions/advance! state-map))}
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
