(ns advent-of-code-2023.day6
  (:require [clojure.string :as str]))

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

(defn solve-part-2
  "This is still kinda slow. There must be a maths trick to cut down the time."
  [lines]
  (let [race (parse-one-race lines)]
    (winning-strategies race)))