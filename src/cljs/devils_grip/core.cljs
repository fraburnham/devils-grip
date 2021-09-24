(ns devils-grip.core
  (:require
   [reagent.core :as reagent]
   [devils-grip.actions.engine :as actions]
   [devils-grip.components.action :as action]
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
      (action/button state-map :draw "Draw")
      (action/button state-map :from-talon "Place from talon")
      [:br]
      (action/button state-map :swap-cells "Swap cells")
      (action/button state-map :merge-cells "Merge cells")
      [:br]
      (action/button state-map :abort "Abort action")
      [:br]
      (action/button state-map :start "New game")]]]])

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
