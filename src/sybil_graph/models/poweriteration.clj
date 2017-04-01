(ns sybil_graph.models.poweriteration
  (:require [clojurewerkz.neocons.rest        :as nr]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [sybil_graph.models.neo4j         :as neo4j]))
; this one should return something else
(def get-neighbors-query "MATCH (graph:Graph)-[:HAS_NODE]->(node:Node)-[:CONNECT]->(neighbors:Node)
                          WHERE graph.name = {graphName} AND node.id = {nodeId}
                          RETURN node, neighbors;")

(def add-temptrust-to-neighbors-query "MATCH (graph:Graph)-[:HAS_NODE]->(node:Node)-[:CONNECT]->(ngb:Node)
                                      WITH graph, node, COLLECT(ngb) AS neighbors
                                      WHERE graph.name = {graphName} AND node.id = {nodeId}
                                      FOREACH (n IN neighbors | SET n.tempTrust = n.tempTrust + {tempTrust} )
                                      RETURN neighbors;")

(def add-trust-to-node-query "MATCH (graph:Graph)-[:HAS_NODE]->(node:Node)
                              WHERE node.id = {nodeId} AND graph.name = {graphName}
                              SET node.tempTrust = node.tempTrust + {tempTrust}
                              SET node.trust = node.trust + {trust}
                              RETURN node;")

(def set-temptrust-to-trust-query "MATCH (graph:Graph)-[:HAS_NODE]->(node:Node)
                                  WITH graph, COLLECT(node) as nodes
                                  WHERE graph.name = {graphName}
                                  FOREACH (n IN nodes |
                                  SET n.trust = n.tempTrust
                                  SET n.tempTrust = 0 )
                                  RETURN graph;")

(defn add-temptrust-to-neighbors
  [graphName nodeId value]
  (first (cy/tquery neo4j/conn add-temptrust-to-neighbors-query
    {:graphName graphName :nodeId nodeId :tempTrust value})))

(defn get-neighbors
  [graphName nodeId]
  (cy/tquery neo4j/conn get-neighbors-query {:graphName graphName :nodeId nodeId}))

(defn add-trust-to-node
  [graphName nodeId trust tempTrust]
 (cy/tquery neo4j/conn add-trust-to-node-query
   {:graphName graphName :nodeId nodeId :trust trust :tempTrust tempTrust}))

(defn set-temptrust-to-trust
  [graphName]
  (cy/tquery neo4j/conn set-temptrust-to-trust-query {:graphName graphName}))
