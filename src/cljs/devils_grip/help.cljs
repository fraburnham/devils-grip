(ns devils-grip.help)

(defn help [action-state]
  (:help-text @action-state))

(defn error [action-state]
  (:error @action-state))
