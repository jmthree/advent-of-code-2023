(ns advent-of-code-2023.day3
  (:require [clojure.set :as set]))

; part 1
(defn- classify-char [^Character c]
  (cond
    (= \. c) :ignored
    (= \* c) :gears
    (Character/isDigit c) :digits
    :else :symbols))

(defn- neighbors-set [[x y] span]
  (let [xs (range (- x 1) (+ x span 1))]
    (set
      (concat
        (map #(vector % (- y 1)) xs)
        (list [(first xs) y] [(last xs) y])
        (map #(vector % (+ y 1)) xs)))))

(defn- tokenize-line [line]
  (let [index-and-classify
        (fn [idx char] {:char char :start idx :type (classify-char char)})
        partition->token
        (fn [[{:keys [type start]} & _ :as partition]]
          {:start start
           :type  type
           :token (apply str (map :char partition))})]
    (->> line
         (map-indexed index-and-classify)
         (partition-by :type)
         (map partition->token))))

(defn- token->symbol [line-no {start :start}]
  [start line-no])

(defn- token->number [line-no {:keys [start token]}]
  {:pos  [start line-no]
   :span (count token)
   :val  (Integer/parseInt token)})

(defn- tokens->symbols+numbers [line-no tokens]
  (let [token->symbol (partial token->symbol line-no)
        token->number (partial token->number line-no)
        {:keys [digits gears symbols]} (group-by :type tokens)]
    {:symbols (set (map token->symbol (concat gears symbols)))
     :numbers (map token->number digits)}))

(defn- merge-cols [l r]
  (if (set? l)
    (set/union l r)
    (into l r)))

(defn- lines->numbers+symbols [lines]
  (->> lines
       (map tokenize-line)
       (map-indexed #(tokens->symbols+numbers %1 %2))
       (apply merge-with merge-cols)))

(defn- number-worth [{:keys [pos span val]} all-symbols]
  (let [num-neighbors (neighbors-set pos span)
        next-to-symbol (not-empty (set/intersection all-symbols num-neighbors))]
    (if next-to-symbol val 0)))

(defn solve-part-1 [lines]
  (let [{:keys [symbols numbers]} (lines->numbers+symbols lines)]
    (reduce
      (fn [acc number] (+ (number-worth number symbols) acc))
      0
      numbers)))

; part 2
(defn- tokens->gears+numbers [line-no tokens]
  (let [token->gear (partial token->symbol line-no)
        token->number (partial token->number line-no)
        {:keys [digits gears]} (group-by :type tokens)]
    {:gears   (set (map token->gear gears))
     :numbers (map token->number digits)}))

(defn- lines->gears+numbers [lines]
  (->> lines
       (map tokenize-line)
       (map-indexed #(tokens->gears+numbers %1 %2))
       (apply merge-with merge-cols)))

(defn- gears-neighboring-number [gears {:keys [pos span val]}]
  (let [num-neighbors (neighbors-set pos span)
        neighbor-gears (set/intersection gears num-neighbors)]
    (map #(hash-map % (list val)) neighbor-gears)))

(defn- gears-to-neighboring-numbers [gears numbers]
  (->> numbers
       (mapcat #(gears-neighboring-number gears %))
       (apply merge-with into)))

(defn solve-part-2 [lines]
  (let [{:keys [gears numbers]} (lines->gears+numbers lines)
        gears-w-neighbors (gears-to-neighboring-numbers gears numbers)
        possible-ratios (->> gears-w-neighbors (vals) (filter #(= 2 (count %))))
        gear-ratios (map #(apply * %) possible-ratios)]
    (reduce + 0 gear-ratios)))
