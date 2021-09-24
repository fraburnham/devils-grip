(ns devils-grip.components.help)

(defn help [action-state]
  (:help-text @action-state))

(defn error [action-state]
  (:error @action-state))
