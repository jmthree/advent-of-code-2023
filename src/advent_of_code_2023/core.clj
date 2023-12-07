(ns advent-of-code-2023.core
  (:require [clojure.java.io :as io]))

(defn resource-lines! [f]
  (with-open [r (io/reader (io/resource f))]
    (into [] (line-seq r))))
