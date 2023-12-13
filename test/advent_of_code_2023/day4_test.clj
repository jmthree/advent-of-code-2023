(ns advent-of-code-2023.day4-test
  (:require [advent-of-code-2023.core :as c]
            [advent-of-code-2023.day4 :refer :all]
            [clojure.test :refer :all]))

(def sample-lines (c/resource-lines! "day4-sample.txt"))
(def solution-lines (c/resource-lines! "day4.txt"))

(def sample-solution 13)
(def part-1-solution 20407)
(def part-2-solution 23806951)

(deftest sample
  (is (= (solve-part-1 sample-lines) sample-solution)))

(deftest part-1
  (is (= (solve-part-1 solution-lines) part-1-solution)))

(deftest part-2
  (is (= (solve-part-2 solution-lines) part-2-solution)))