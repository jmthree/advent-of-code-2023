(ns advent-of-code-2023.day1
  (:require [clojure.string :as s]))

;; Part 1
(defn- extract-coord-digits [s]
  (let [digits (->> s
                    (filter #(Character/isDigit %))
                    (map #(Character/digit % 10)))]
    (+ (* 10 (first digits)) (last digits))))

(defn solve-part-1 [lines]
  (->> lines
       (map extract-coord-digits)
       (reduce +)))

;; part 2
(def words-to-numbers
  {"zero"  0
   "one"   1
   "two"   2
   "three" 3
   "four"  4
   "five"  5
   "six"   6
   "seven" 7
   "eight" 8
   "nine"  9})

(defn- grams [s]
  (reduce
    (fn [acc c] (conj acc (str (last acc) c)))
    []
    s))

(def valid-states
  (set (concat (map str (vals words-to-numbers))
               (mapcat grams (keys words-to-numbers)))))

(def end-states
  (set (concat (map str (vals words-to-numbers))
               (keys words-to-numbers))))

(defn- number-likes-from-str [s]
  (loop [found []
         states []
         letters s]
    (if (empty? letters)
      found
      (let [p (subs letters 0 1)
            r (subs letters 1)]
        (if (contains? end-states p)
          (recur (conj found p) [] r)
          (if (empty? states)
            (recur found [p] r)
            (let [new-states (conj (mapv #(str % p) states) p)
                  potential (first new-states)]
              (if (contains? end-states potential)
                (recur (conj found potential) (filterv #(contains? valid-states %) new-states) r)
                (recur found (filterv #(contains? valid-states %) new-states) r)))))))))

(defn- number-like-to-number [s]
  (or (words-to-numbers s)
      (Integer/parseInt s)))

(defn- extract-coord-parsed [s]
  (let [ns (number-likes-from-str s)
        digits (mapv number-like-to-number ns)]
    (+ (* 10 (first digits)) (last digits))))

(defn solve-part-2 [lines]
  (->> lines
       (map extract-coord-parsed)
       (reduce +)))

;; part 2 simpler
(def all-numbers
  {"0"     0
   "1"     1
   "2"     2
   "3"     3
   "4"     4
   "5"     5
   "6"     6
   "7"     7
   "8"     8
   "9"     9
   "zero"  0
   "one"   1
   "two"   2
   "three" 3
   "four"  4
   "five"  5
   "six"   6
   "seven" 7
   "eight" 8
   "nine"  9})

(defn- first-number-in-str [s]
  (get
    all-numbers
    (second
      (apply min-key
             first
             (map
               (fn [t] [(or (s/index-of s t) Integer/MAX_VALUE) t])
               (keys all-numbers))))))

(defn- last-number-in-str [s]
  (get
    all-numbers
    (second
      (apply max-key
             first
             (map
               (fn [t] [(or (s/last-index-of s t) Integer/MIN_VALUE) t])
               (keys all-numbers))))))

(defn- extract-coord-indexof [s]
  (+ (* 10 (first-number-in-str s)) (last-number-in-str s)))

(defn solve-part-2-simpler [lines]
  (->> lines
       (map extract-coord-indexof)
       (reduce +)))

(comment
  (require '[advent-of-code-2023.core :as c])
  (use '(criterium.core))

  (def lines (c/resource-lines! "day1.txt"))
  (with-progress-reporting (bench (solve-part-2 lines) :verbose))
  (with-progress-reporting (bench (solve-part-2-simpler lines) :verbose)))
