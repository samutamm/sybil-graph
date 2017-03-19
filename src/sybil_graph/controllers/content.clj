(ns sybil_graph.controllers.content
 (:require [sybil_graph.models.sybils     :as sybils]))

(defn create-graph
  [name]
  (sybils/create-graph name))

(defn get-graphs
  []
  (sybils/get-all-graphs))
