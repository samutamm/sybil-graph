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
          (< i steps) (let [next (nth neighbors (rand-int (count neighbors)))
                             nextId  (get-in next [:data :id])]
                         (recur (inc i) nextId next))
          :else (do
                  (randomwalk/clean-visited-flags graphName)
                  {:stepsTaken i :node currentNode})))))

(defn get-nodes-by-factor
  [nodes factor]
  (let [nodeCount (int nodes)]
    (cond
      (= "log" factor) (Math/round (Math/log nodeCount))
      :else (int (* (read-string factor) nodeCount)))))

(defn simulate-randomwalk
  [params]
  (let [nodeCount (Integer/parseInt (:nodes params))
        steps (get-nodes-by-factor nodeCount (:stepfactor params))
        seedCount (Integer/parseInt (:seeds params))]
    (loop [i 0
           resultArray []]
        (if (< i seedCount)
          (recur
            (inc i)
            (let [results (walk-n-steps (:graphName params) (rand-int nodeCount) steps)]
              (conj resultArray {:impasse (< (:stepsTaken results) steps)
                                 :node (get-in results [:node :data])})))
          resultArray))))

(defn do-randomwalk
  [params]
  (let [result (simulate-randomwalk params)
        sybils (count (filter (fn[r] (get-in r [:node :sybil])) result))
        impasses (count (filter (fn[r] (:impasse r)) result))
        toSave {:sybils sybils
                :impasses impasses
                :sybilPercent (/ sybils (count result))
                :impassePercent (/ impasses (count result))}]
      (get-in (randomwalk/save-randomwalk toSave) ["randomwalk" :metadata :id])))

(defn get-randomwalk
  [id]
  (get-in (randomwalk/get-randomwalk-by-id (Integer/parseInt (str id))) ["randomwalk" :data]))
