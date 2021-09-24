(ns devils-grip.components.action
  (:require
   [devils-grip.actions.engine :as actions]))

(defn button [{:keys [action-state] :as state-map} action text]
  [:button
   {:on-click
    (fn [_]
      (actions/action-click! action-state action)
      (actions/advance! state-map))}
   text])
