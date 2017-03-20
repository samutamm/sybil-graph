(ns sybil_graph.controllers.content
 (:require [sybil_graph.models.sybils     :as sybils]))

(defn create-graph
  [name]
  (sybils/create-graph name))

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
  (do (println (str "-------------------------" id "------------------------"))(sybils/remove-graph (str id))))
