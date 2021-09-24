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

(defonce state-map
  {:action-state (reagent/atom {}) ; refactor the key names to not have `-state`
   :stock (reagent/atom nil)
   :talon (reagent/atom [])
   :board-state (reagent/atom [])})

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
       (partial actions/selection-click! state-map)
       state-map)]
     [:td {:id "help"
           :style {:font-style "italic" :color "blue"}}
      (help/help (:action-state state-map))]
     [:td {:id "error"
           :style {:font-style "italic" :color "red"}}
      (help/error (:action-state state-map))]]
    [:tr
     [:td
      (board/stock state-map)
      " " ; probably a better way to do this w/ styles
      (board/talon state-map)
      " "
      (board/score state-map)]]
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
