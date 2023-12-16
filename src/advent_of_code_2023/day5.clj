(ns advent-of-code-2023.day5
  (:require [clojure.string :as str]))

; part 1
(defn- parse-seeds [[line]]
  (as-> line $
        (str/split $ #":\s+")
        (second $)
        (str/split $ #"\s+")
        (map #(Long/parseLong %) $)))

(defn- parse-mapping [mapping]
  (let [pieces (str/split mapping #"\s+")
        [dest-start source-start span] (map #(Long/parseLong %) pieces)]
    {:domain    [source-start (+ source-start span -1)]
     :transform (- dest-start source-start)}))

(defn- parse-mapping-section [[name-line & mappings]]
  (let [[key _] (str/split name-line #"\s+")]
    [(keyword key) (map parse-mapping mappings)]))

(defn- parse-almanac [lines]
  (let [[seeds & mappings] (filter #(not-empty (first %))
                                   (partition-by empty? lines))
        seeds (parse-seeds seeds)
        mappings (map parse-mapping-section mappings)]
    (into {:seeds seeds} mappings)))

(defn- apply-mapping [i {:keys [domain transform]}]
  (let [[d-start d-end] domain]
    (if (<= d-start i d-end)
      (+ transform i)
      nil)))

(defn- convert-via-mappings [i mappings]
  (if-let [converted (->> mappings
                          (map #(apply-mapping i %))
                          (filter identity)
                          (first))]
    converted
    i))

(def ^:private mappings-order
  [:seed-to-soil
   :soil-to-fertilizer
   :fertilizer-to-water
   :water-to-light
   :light-to-temperature
   :temperature-to-humidity
   :humidity-to-location])

(defn- seed->location [seed almanac]
  (reduce
    (fn [i mappings-key]
      (let [mappings (get almanac mappings-key)]
        (convert-via-mappings i mappings)))
    seed
    mappings-order))

(defn solve-part-1 [lines]
  (let [almanac (parse-almanac lines)]
    (->> almanac
         (:seeds)
         (map #(seed->location % almanac))
         (apply min))))

; part 2
(defn- seeds->domains [seeds]
  (map (fn [[start span]]
         [start (+ start span -1)])
       (partition 2 seeds)))

(defn domain->mapped-domains [source-domain mappings]
  (loop [source-d source-domain
         mappings (sort-by (comp first :domain) mappings)
         mapped []]
    (if (empty? mappings)
      (conj mapped source-d)
      (let [{[m-start m-end] :domain transform :transform} (first mappings)
            [s-start s-end] source-d
            s->m #(+ % transform)]
        (cond
          (<= m-start s-start s-end m-end)
          (conj mapped [(s->m s-start) (s->m s-end)])

          (< s-start m-start m-end s-end)
          (recur
            [m-start s-end]
            mappings
            (conj mapped
                  [s-start (- m-start 1)]))

          (< s-end m-start)
          (conj mapped source-d)

          (> s-start m-end)
          (recur source-d
                 (rest mappings)
                 mapped)

          (<= s-start m-start s-end m-end)
          (recur
            [m-start s-end]
            mappings
            (conj mapped
                  [s-start (- m-start 1)]))

          (<= m-start s-start m-end s-end)
          (recur
            [(+ m-end 1) s-end]
            mappings
            (conj mapped
                  [(s->m s-start) (s->m m-end)])))))))

(defn solve-part-2 [lines]
  (let [almanac (parse-almanac lines)
        seed-domains (seeds->domains (:seeds almanac))
        mapped-domains
        (reduce
          (fn [domains mappings-key]
            (let [mappings (get almanac mappings-key)]
              (mapcat #(domain->mapped-domains % mappings) domains)))
          seed-domains
          mappings-order)]
    (ffirst (sort-by first mapped-domains))))