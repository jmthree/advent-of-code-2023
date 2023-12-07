(ns advent-of-code-2023.day2-test
  (:require [advent-of-code-2023.core :as c]
            [advent-of-code-2023.day2 :refer :all]
            [clojure.test :refer :all]))

(def lines (c/resource-lines! "day2.txt"))
(def part-1-solution 2593)
(def part-2-solution 54699)

(deftest part-1
  (is (= (solve-part-1 lines) part-1-solution)))

(deftest part-2
  (is (= (solve-part-2 lines) part-2-solution)))

