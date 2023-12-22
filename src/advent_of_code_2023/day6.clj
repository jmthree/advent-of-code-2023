(ns advent-of-code-2023.day6
  (:require [clojure.math :as m]
            [clojure.string :as str]))

;part1
(defn- parse-races [[times-line bests-line]]
  (let [[_ & times] (str/split times-line #"\s+")
        [_ & bests] (str/split bests-line #"\s+")
        times (map #(Long/parseLong %) times)
        bests (map #(Long/parseLong %) bests)]
    (map vector times bests)))

(def ^:private mm-per-second-per-second 1)

(defn- winning-strategies [[total-time target-distance]]
  (let [start (quot total-time 2)
        hold-times (take-while pos? (iterate dec start))
        time->distance (fn [hold-time]
                         (* mm-per-second-per-second
                            hold-time
                            (- total-time hold-time)))
        winning-distances (take-while
                            #(> % target-distance)
                            (map time->distance hold-times))
        symmetrical-modifier (if (even? total-time) -1 0)]
    (+ (* 2 (count winning-distances))
       symmetrical-modifier)))

(defn solve-part-1 [lines]
  (let [races (parse-races lines)
        total-winning-ways (map winning-strategies races)]
    (reduce * 1 total-winning-ways)))

;part2
(defn- parse-one-race [[times-line bests-line]]
  (let [[_ & time-parts] (str/split times-line #"\s+")
        [_ & best-parts] (str/split bests-line #"\s+")
        time (Long/parseLong (apply str time-parts))
        best (Long/parseLong (apply str best-parts))]
    [time best]))

(defn solve-part-2-slow
  [lines]
  (let [race (parse-one-race lines)]
    (winning-strategies race)))

;part2 efficient
(defn- solve-as-quadratic
  "Solves for x in x(t - x) = d + 1, which is 1 more than the target distance."
  [time target-distance]
  (let [b time
        c (inc target-distance)]
    (sort [(/ (+ b (m/sqrt (- (m/pow b 2) (* 4 c)))) 2)
           (/ (- b (m/sqrt (- (m/pow b 2) (* 4 c)))) 2)])))

(defn solve-part-2 [lines]
  (let [[time max-distance] (parse-one-race lines)
        [low high] (solve-as-quadratic time max-distance)
        bounded-int-low (int (m/ceil low))
        bounded-int-high (int (m/floor high))]
    (- (inc bounded-int-high) bounded-int-low)))