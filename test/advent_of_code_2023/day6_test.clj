(ns advent-of-code-2023.day6-test
  (:require [advent-of-code-2023.core :as c]
            [advent-of-code-2023.day6 :refer :all]
            [clojure.test :refer :all]))

(def sample-lines (c/resource-lines! "day6-sample.txt"))
(def solution-lines (c/resource-lines! "day6.txt"))

(def part-1-sample-solution 288)
(def part-1-solution 2449062)
(def part-2-sample-solution 71503)
(def part-2-solution 33149631)

(deftest part-1
  (is (= (solve-part-1 sample-lines) part-1-sample-solution))
  (is (= (solve-part-1 solution-lines) part-1-solution)))

(deftest part-2
  (is (= (solve-part-2 sample-lines) part-2-sample-solution))
  (is (= (solve-part-2 solution-lines) part-2-solution)))
