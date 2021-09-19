(ns devils-grip.cards)

(def ranks [:2 :3 :4 :5 :6 :7 :8 :9 :10 :J :Q :K])

(def suits [:H :S :C :D])

(def rank->value
  {:K 13
   :Q 12
   :J 11})

(def suit->color
  {:H "red"
   :D "red"
   :C "black"
   :S "black"})

(def suit->symbol
  {:H "\u2665"
   :D "\u2666"
   :C "\u2663"
   :S "\u2660"})


(defn drop-nth [n col]
  (concat (take n col) (drop (inc n) col)))

(defn random-index [total-count]
  (Math/floor (* (rand) total-count)))

(defn make-stock [num-decks]
  (mapcat
   (fn [_]
     (for [suit suits rank ranks] [suit rank]))
   (range num-decks)))

(defn shuffle-stock
  ([cards]
   (let [n (random-index (count cards))]
     (shuffle-stock (drop-nth n cards) (vector (nth cards n)))))
  ([cards stock]
   (if (not-empty cards)
     (let [n (random-index (count cards))]
       (shuffle-stock (drop-nth n cards) (conj stock (nth cards n))))
     stock)))
