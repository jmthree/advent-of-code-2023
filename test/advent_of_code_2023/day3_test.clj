(ns advent-of-code-2023.day3-test
  (:require [advent-of-code-2023.core :as c]
            [advent-of-code-2023.day3 :refer :all]
            [clojure.test :refer :all]))

(def sample-lines (c/resource-lines! "day3-sample.txt"))
(def solution-lines (c/resource-lines! "day3.txt"))

(def sample-solution 4361)
(def part-1-solution 498559)
(def part-2-solution 72246648)

(deftest sample
  (is (= (solve-part-1 sample-lines) sample-solution)))

(deftest part-1
  (is (= (solve-part-1 solution-lines) part-1-solution)))

(deftest part-2
  (is (= (solve-part-2 solution-lines) part-2-solution)))