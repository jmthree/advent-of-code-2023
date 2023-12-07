(ns advent-of-code-2023.day2
  (:require [clojure.string :as str]))

;; part 1
(def global-constraints
  {:red   12
   :green 13
   :blue  14})

(defn- parse-color [s]
  (let [[count color] (str/split s #" ")]
    [(keyword color) (Integer/parseInt count)]))

(defn- parse-game-id [s]
  (let [[_ game-id] (str/split s #" ")]
    (Integer/parseInt game-id)))

(defn- parse-reveal [s]
  (let [colors (str/split s #", ")
        kv (map parse-color colors)]
    (into (hash-map) kv)))

(defn- parse-game [s]
  (let [[game-id joined-reveals] (str/split s #": ")
        reveals (str/split joined-reveals #"; ")]
    {:id      (parse-game-id game-id)
     :reveals (map parse-reveal reveals)}))

(defn- valid-reveal? [constraints reveal]
  (every?
    (fn [[color count]]
      (>= (get constraints color) count))
    reveal))

(defn- valid-game? [constraints game]
  (every?
    #(valid-reveal? constraints %)
    (:reveals game)))

(defn solve-part-1 [lines]
  (->> lines
       (map parse-game)
       (filter #(valid-game? global-constraints %))
       (map :id)
       (reduce +)))

;; part 2
(defn- game-power [g]
  (->> g
       (:reveals)
       (apply merge-with max)
       (vals)
       (apply *)))

(defn solve-part-2 [lines]
  (->> lines
       (map parse-game)
       (map game-power)
       (reduce +)))
