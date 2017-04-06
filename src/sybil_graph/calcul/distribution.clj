(ns sybil_graph.calcul.distribution
  (:require [incanter.core        :as incanter]
            [incanter.stats       :as stats]
            [incanter.charts      :as charts]
            [incanter.io          :as io]
            [incanter.pdf         :as pdf])
            (:gen-class))

(def default-params
  {:n 1000 :mean 99 :sd 70})

;(defn average
;  [numbers]
;  (/ (sum numbers) (count numbers)))

(defn create-histogram
  [params]
  (charts/histogram
    (filter (fn[x] (< 1 x))
    (stats/sample-normal (:n params) :mean (:mean params) :sd (:sd params)))
  :x-label "Degree" ))

(defn pdf-sample-normal
  [params]
  (pdf/save-pdf (create-histogram params) "./sample-normal.pdf" ))

(defn view-sample-normal
  [params]
  (incanter/view
    (create-histogram params)))
