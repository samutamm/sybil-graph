(ns sybil_graph.controllers.randomwalk
 (:require [sybil_graph.models.randomwalk    :as randomwalk]))

(defn walk-n-steps
  [graphName startId steps]
  (loop [i 0
         currentNodeId startId
         currentNode nil]
      (let [neighbors (get (randomwalk/get-neighbors graphName currentNodeId) "neighbors" )]
        (cond
          (= (count neighbors) 0) {:stepsTaken i :node currentNode}
          (<= i steps) (let [next (nth neighbors (rand-int (count neighbors)))
                             nextId  (get-in next [:data :id])]
                         (recur (inc i) nextId next))
          :else (do
                  (randomwalk/clean-visited-flags graphName)
                  {:stepsTaken i :node currentNode})))))

(defn simulate-randomwalk
  [params]
  ())
