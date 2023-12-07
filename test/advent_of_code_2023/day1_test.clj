(ns advent-of-code-2023.day1-test
  (:require [advent-of-code-2023.core :as c]
            [advent-of-code-2023.day1 :refer :all]
            [clojure.test :refer :all]))

(def lines (c/resource-lines! "day1.txt"))
(def part-1-solution 53194)
(def part-2-solution 54249)

(deftest part-1
  (is (= (solve-part-1 lines) part-1-solution)))

(deftest part-2
  (testing "original"
    (is (= (solve-part-2 lines) part-2-solution)))
  (testing "simpler"
    (is (= (solve-part-2-simpler lines) part-2-solution))))

