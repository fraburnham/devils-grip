(ns devils-grip.core
  (:require
   [reagent.core :as reagent]
   [devils-grip.actions.engine :as actions]
   [devils-grip.components.action :as action]
   [devils-grip.components.board :as board]
   [devils-grip.components.help :as help]))

(defonce state-map
  {:action-state (reagent/atom {}) ; refactor the key names to not have `-state`
   :stock (reagent/atom nil)
   :talon (reagent/atom [])
   :board-state (reagent/atom [])})

(defn page [ratom]
  ;; divs may be nicer but tables are easy
  [:table
   [:tbody
    [:tr
     [:td {:id "title"} "Devil's Grip Solitaire"]]
    [:tr
     [:td {:id "board"}
      (board/board
       (fn [] (actions/advance! state-map))
       (partial actions/selection-click! state-map)
       state-map)]
     [:td {:id "actions"}
      (action/button state-map :draw "Draw")
      (action/button state-map :from-talon "Place from talon")
      [:br]
      (action/button state-map :swap-cells "Swap cells")
      (action/button state-map :merge-cells "Merge cells")
      [:br]
      (action/button state-map :abort "Abort action")
      [:br]
      (action/button state-map :start "New game")]]
    [:tr
     [:td
      [:div
       [:span {:id "help"
               :style {:font-style "italic" :color "blue"}}
        (help/help (:action-state state-map))]
       [:span {:id "error"
               :style {:font-style "italic" :color "red"}}
        (help/error (:action-state state-map))]]
      [:div
       (board/stock state-map)
       [:br]
       (board/talon state-map)
       [:br]
       (board/score state-map)]]]]])

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn reload []
  (reagent/render [page] (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload))
