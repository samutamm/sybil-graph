(ns sybil_graph.models.poweriteration
  (:require [clojurewerkz.neocons.rest        :as nr]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [sybil_graph.models.neo4j         :as neo4j]))

(def get-neighbors-query "MATCH (graph:Graph)-[:HAS_NODE]->(node)-[:CONNECT]->(neighbors)
                          WHERE graph.name = {graphName} AND node.id = {nodeId}
                          RETURN node, neighbors;")

(def add-temptrust-to-node-query "MATCH (graph:Graph)-[:HAS_NODE]->(node:Node)
                                  WHERE node.id = {102} AND graph.name = {gen}
                                  SET node.tempTrust = node.tempTrust + 21
                                  RETURN node;")

(defn get-neighbors
  [graphName nodeId]
  (first (cy/tquery neo4j/conn get-neighbors-query {:graphName graphName :nodeId nodeId})))

(defn add-temptrust-to-node
  [graphName nodeId]
 (cy/tquery neo4j/conn add-temptrust-to-node-query {:graphName graphName :nodeId nodeId}))

; TEST THESE!
