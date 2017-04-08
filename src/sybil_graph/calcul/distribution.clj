(ns sybil_graph.calcul.distribution
  (:require ;[incanter.core        :as incanter]
            [incanter.stats       :as stats]
            [incanter.charts      :as charts]
            [incanter.pdf         :as pdf])
            (:gen-class))

(defn average
  [numbers]
  (/ (reduce + numbers) (count numbers)))

(defn power-law-generator [power] (nl.peterbloem.powerlaws.Discrete. 1 power))

(defn generate-one-from-powerlaw
  [max power]
  (loop [number (inc max)]
    (if (< number max)
      number
      (recur (.generate (power-law-generator power))))))

(defn histogram-power-law
  [params]
  (let [numbers (filter
                  (fn[x] (< x (:max params)))
                    (map
                      (fn[x] (.generate (power-law-generator (:power params))))
                    (range (:n params))))
        avg (average numbers)]
    (do
      (pdf/save-pdf (charts/histogram numbers) "./power-law.pdf")
      (str "avg: " avg ", count: " (count numbers)))))

(defn sample-one-from-distribution
  [dist params]
  (cond
    (= dist "normal-distribution") (stats/sample-normal 1 :mean (:mean params) :sd (:sd params))
    (= dist "power-law") (generate-one-from-powerlaw (:max params) (:power params))))

(def default-params
  {:n 1000 :mean 99 :sd 70})

(defn create-histogram
  [params]
  (charts/histogram
    (filter (fn[x] (< 1 x))
      (stats/sample-normal (:n params) :mean (:mean params) :sd (:sd params)))
  :x-label "Degree" ))

(defn pdf-sample-normal
  [params]
  (pdf/save-pdf (create-histogram params) "./sample-normal.pdf" ))

;(defn view-sample-normal
;  [params]
;  (incanter/view
;    (create-histogram params)))
