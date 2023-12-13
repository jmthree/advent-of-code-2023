(ns advent-of-code-2023.day4
  (:require [clojure.set :as set]
            [clojure.string :as str]))

; part 1
(defn- line->card [line]
  (let [[id numbers] (str/split line #":\s+")
        [_ id] (str/split id #"\s+")
        id (Integer/parseInt id)
        [winning have] (str/split numbers #"\s+\|\s+")
        winning (set (str/split winning #"\s+"))
        have (set (str/split have #"\s+"))]
    {:id id :have have :winning winning}))

(defn- card-worth [{:keys [winning have]}]
  (let [matches (set/intersection winning have)]
    (if (empty? matches)
      0
      (Math/toIntExact (Math/pow 2 (- (count matches) 1))))))

(defn solve-part-1 [lines]
  (->> lines
       (map line->card)
       (map card-worth)
       (reduce + 0)))

;part 2
(defn- card->new-winnings [{:keys [id winning have]}]
  (let [matches (set/intersection winning have)]
    (range (+ id 1) (+ id (count matches) 1))))

(defn- build-cards-to-new-winnings-table [cards]
  (let [card->entry
        (fn [card] [(:id card) (card->new-winnings card)])]
    (into (sorted-map) (map card->entry cards))))

(def ^:private card->total-cards-it-wins
  (memoize
    (fn [card-id cards-to-new-winnings]
      (let [winnings (get cards-to-new-winnings card-id)
            winning->winnings #(card->total-cards-it-wins % cards-to-new-winnings)]
        (apply
          +
          (count winnings)
          (map winning->winnings winnings))))))

(defn solve-part-2 [lines]
  (let [cards-to-new-winnings (->> lines
                                   (map line->card)
                                   (build-cards-to-new-winnings-table))
        original-cards (keys cards-to-new-winnings)
        card->total-winnings #(card->total-cards-it-wins % cards-to-new-winnings)]
    (apply
      +
      (count original-cards)
      (map card->total-winnings original-cards))))