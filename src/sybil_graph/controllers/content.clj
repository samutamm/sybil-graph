(ns sybil_graph.controllers.content
 (:require [sybil_graph.models.sybils     :as sybils]
           [sybil_graph.config            :as config]
           [sybil_graph.calcul.distribution :as dist]))

(defn create-n-nodes
  [params]
  (loop [i (:firstId params)]
    (do
      (sybils/add-node-to-graph (:graphName params) i (:isSybil params))
      (if (not (>= i (:lastId params)))
        (recur (inc i))))))

(defn generate-random-ids
  [currentId params]
  (let [until (dist/sample-one-from-distribution "normal-distribution"
                { :mean (:normalDistMean config/default-config)
                  :sd (:normalDistSD config/default-config)
                  :max (:maxDegree config/default-config)
                  :power (:powerlaw config/default-config)})]
    (loop [i 0
           ids []]
           (let [newRandom (+ (rand-int (- (:lastId params)(:firstId params))) (:firstId params))]
             (cond
               (> i until) ids
               (= currentId newRandom) (recur (inc i) ids)
               :else  (recur
                        (inc i)
                        (conj ids newRandom)))))))

(defn create-relations-to-nodes
  [params]
  (loop [i (:firstId params)]
    (let [randoms (generate-random-ids i params)]
      (do
        (sybils/create-relations (:graphName params) i randoms)
        (if (not (= i (:lastId params)))
          (recur (inc i)))))))

(defn add-attack-edges
  [params]
  (loop [i 0]
    (let [fromNode (+ (:sybilFirst params)(rand-int (- (:sybilLast params)(:sybilFirst params))))
          toNode (+ (:firstId params)(rand-int (- (:lastId params)(:firstId params))))]
      (do
        (sybils/add-attack-edge (:graphName params) fromNode toNode)
        (if (< i (:attackedges params))
          (recur (inc i)))))))

(defn create-graph
  [name nodesFrequency sybilsFrequency attackedges]
  (let [firstId (:firstId config/default-config)
        lastId (:lastId config/default-config)
        sybilFirst (:sybilFirst config/default-config)
        sybilLast (:sybilLast config/default-config)] ;TODO what is the default size of sybil attacks
    (do
      (sybils/create-graph name (inc (- lastId firstId)))
      (create-n-nodes {:graphName name
                       :firstId firstId
                       :lastId lastId
                       :isSybil false})
      (create-relations-to-nodes {:graphName name
                                  :firstId firstId
                                  :lastId lastId})
      (create-n-nodes {:graphName name
                       :firstId sybilFirst
                       :lastId sybilLast
                       :isSybil true})
      (create-relations-to-nodes {:graphName name
                                  :firstId sybilFirst
                                  :lastId sybilLast })
      (add-attack-edges {:graphName name
                        :firstId firstId
                        :lastId lastId
                        :sybilFirst sybilFirst
                        :sybilLast sybilLast
                        :attackedges attackedges}))))

(defn get-graphs
  []
  (map
    (fn[graphObject]
      (let [graph (get graphObject "graph")]
        { :id (get-in graph [:metadata :id])
          :data (get graph :data)
          :nodes (get graphObject "normalNodes")}))
  (sybils/get-all-graphs)))

(defn remove-graph
  [id]
  (sybils/remove-graph (str id)))
