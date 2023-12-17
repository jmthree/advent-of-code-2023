(ns advent-of-code-2023.day7
  (:require [clojure.string :as str]))

;part1
(defn- parse-hand-and-bid [line]
  (let [[hand bid] (str/split line #"\s+")]
    [hand (Long/parseLong bid)]))

(def ^:private card-values
  {\A 1
   \K 2
   \Q 3
   \J 4
   \T 5
   \9 6
   \8 7
   \7 8
   \6 9
   \5 10
   \4 11
   \3 12
   \2 13})

(defn card-sort [a b]
  (- (get card-values a)
     (get card-values b)))

(defn- shape->type [largest-group total-groups]
  (case largest-group
    5 :five-of-a-kind
    4 :four-of-a-kind
    3 (if (= total-groups 2) :full-house :three-of-a-kind)
    2 (if (= total-groups 3) :two-pair :pair)
    1 :high-card))

(defn- classify-hand [[hand bid]]
  (let [sorted-hand (sort card-sort hand)
        groups (sort-by count > (partition-by identity sorted-hand))
        largest-group (count (first groups))
        total-groups (count groups)
        type (shape->type largest-group total-groups)]
    {:type     type
     :type-ord (vec (map card-values hand))
     :bid      bid}))

(def ^:private hand-values
  {:five-of-a-kind  1
   :four-of-a-kind  2
   :full-house      3
   :three-of-a-kind 4
   :two-pair        5
   :pair            6
   :high-card       7})

(defn- hand-sort [a b]
  (let [a-type-value (get hand-values (:type a))
        b-type-value (get hand-values (:type b))
        type-comp (- a-type-value b-type-value)]
    (if (not= 0 type-comp)
      type-comp
      (compare (:type-ord a) (:type-ord b)))))

(defn solve-part-1 [lines]
  (->> lines
       (map parse-hand-and-bid)
       (map classify-hand)
       (sort hand-sort)
       (map :bid)
       (map * (iterate dec (count lines)))
       (reduce + 0)))

;part2
(def ^:private wild-card-values
  {\A 1
   \K 2
   \Q 3
   \T 4
   \9 5
   \8 6
   \7 7
   \6 8
   \5 9
   \4 10
   \3 11
   \2 12
   \J 13})

(defn wild-card-sort [a b]
  (- (get wild-card-values a)
     (get wild-card-values b)))

(defn- classify-wild-hand [[hand bid]]
  (let [[wildcards rest] ((juxt filter remove) #(= \J %) hand)
        sorted-hand (sort wild-card-sort rest)
        [best-group & rest-groups] (sort-by
                                     count
                                     >
                                     (partition-by identity sorted-hand))
        best-group (concat best-group wildcards)
        largest-group (count best-group)
        total-groups (+ 1 (count rest-groups))
        type (shape->type largest-group total-groups)]
    {:type     type
     :type-ord (vec (map wild-card-values hand))
     :bid      bid}))

(defn solve-part-2 [lines]
  (->> lines
       (map parse-hand-and-bid)
       (map classify-wild-hand)
       (sort hand-sort)
       (map :bid)
       (map * (iterate dec (count lines)))
       (reduce + 0)))