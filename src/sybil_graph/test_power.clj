(ns sybil_graph.test_power
  (:require [sybil_graph.config               :as config]
            [sybil_graph.controllers.content  :as content]
            [sybil_graph.controllers.randomwalk  :as randomwalk]
            [sybil_graph.controllers.poweriteration  :as power]
            [sybil_graph.models.sybils        :as sybil])
            (:gen-class))

(defn simulate
  [graphName attackedges seeds]
  (do
    (content/create-graph graphName 1 1 attackedges)
    (println "GRAPH CREATED!")
    (let [iterations (Math/round (Math/log (:lastId config/default-config)))
          poweriterationId (power/start-iterations
            {:iterations iterations :graphName graphName :seeds seeds})]
    poweriterationId)))


(defn experiment
  [graphName seeds attackedges]
  (map
    (fn[ae]
      (do
        (simulate graphName ae seeds)
        (let [result (power/count-results graphName 100)
              rmv (sybil/delete-all)]
          result)))
    attackedges))
