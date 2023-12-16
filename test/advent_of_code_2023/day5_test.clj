(ns advent-of-code-2023.day5-test
  (:require [advent-of-code-2023.core :as c]
            [advent-of-code-2023.day5 :refer :all]
            [clojure.test :refer :all]))

(def sample-lines (c/resource-lines! "day5-sample.txt"))
(def solution-lines (c/resource-lines! "day5.txt"))

(def part-1-sample-solution 35)
(def part-1-solution 324724204)
(def part-2-sample-solution 46)
(def part-2-solution 104070862)

(deftest part-1
  (is (= (solve-part-1 sample-lines) part-1-sample-solution))
  (is (= (solve-part-1 solution-lines) part-1-solution)))

(deftest part-2
  (is (= (solve-part-2 sample-lines) part-2-sample-solution))
  (is (= (solve-part-2 solution-lines) part-2-solution)))

(deftest domain-mapping
  (is (= (domain->mapped-domains [0 4] [{:domain [4 13] :transform 96}])
         [[0 3] [100 100]])
      "overlap at domain end")
  (is (= (domain->mapped-domains [0 19] [{:domain [21 30] :transform 79}])
         [[0 19]])
      "domain entirely before mapping")
  (is (= (domain->mapped-domains [0 19] [{:domain [1 10] :transform 99}])
         [[0 0] [100 109] [11 19]])
      "domain wraps mapping")
  (is (= (domain->mapped-domains [0 19] [{:domain [21 30] :transform 179}
                                         {:domain [1 10] :transform 99}])
         [[0 0] [100 109] [11 19]])
      "domain wraps and entire before other")
  (is (= (domain->mapped-domains [0 19] [{:domain [15 24] :transform 85}])
         [[0 14] [100 104]])
      "domain starts before and ends inside")
  (is (= (domain->mapped-domains [0 19] [{:domain [0 9] :transform 100}])
         [[100 109] [10 19]])
      "domain starts at and ends after")
  (is (= (domain->mapped-domains [0 99] [{:domain [10 19] :transform 190}
                                         {:domain [30 39] :transform 270}
                                         {:domain [50 56] :transform 350}
                                         {:domain [82 981] :transform 418}])
         [[0 9] [200 209] [20 29] [300 309] [40 49] [400 406] [57 81] [500 517]])
      "domain contains many mappings"))


