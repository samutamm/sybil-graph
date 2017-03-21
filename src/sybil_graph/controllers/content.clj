(ns sybil_graph.controllers.content
 (:require [sybil_graph.models.sybils     :as sybils]))

(defn create-n-nodes
  [graphName n]
  (loop [i 1]
    (do
      (sybils/add-node-to-graph graphName i false)
      (if (not (= i n))
        (recur (inc i))))))

(defn generate-random-ids
  [currentId n]
  (let [until (inc (rand-int 4))]
    (loop [i 0
           ids []]
           (let [newRandom (inc (rand-int (dec n)))]
             (cond
               (> i until) ids
               (= currentId newRandom) (recur (inc i) ids)
               :else  (recur
                        (inc i)
                        (conj ids newRandom)))))))

(defn create-relations-to-nodes
  [graphName n]
  (loop [i 1]
    (let [randoms (generate-random-ids i n)]
      (do
        (println (str "ids: " randoms))
        (if (not (= i n))
          (recur (inc i)))))))
          ; NOW ADD THESE RELATIONS TO GRAPH

(defn create-graph
  [name]
  (let [n 100]
    (do
      (sybils/create-graph name)
      (create-n-nodes name n)
      (create-relations-to-nodes name n))))

(defn get-graphs
  []
  (map
    (fn[x]
      (let [graph (get x "graph")]
        { :id (get-in graph [:metadata :id])
          :data (get graph :data)}))
  (sybils/get-all-graphs)))

(defn remove-graph
  [id]
  (sybils/remove-graph (str id)))
