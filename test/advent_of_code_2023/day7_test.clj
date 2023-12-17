(ns advent-of-code-2023.day7-test
  (:require [advent-of-code-2023.core :as c]
            [advent-of-code-2023.day7 :refer :all]
            [clojure.test :refer :all]))

(def sample-lines (c/resource-lines! "day7-sample.txt"))
(def solution-lines (c/resource-lines! "day7.txt"))

(def part-1-sample-solution 6440)
(def part-1-solution 246163188)
(def part-2-sample-solution 5905)
(def part-2-solution 245794069)

(deftest part-1
  (is (= (solve-part-1 sample-lines) part-1-sample-solution))
  (is (= (solve-part-1 solution-lines) part-1-solution)))

(deftest part-2
  (is (= (solve-part-2 sample-lines) part-2-sample-solution))
  (is (= (solve-part-2 solution-lines) part-2-solution)))
