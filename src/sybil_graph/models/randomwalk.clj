(ns sybil_graph.models.randomwalk
  (:require [clojurewerkz.neocons.rest        :as nr]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [sybil_graph.models.neo4j         :as neo4j]))

(def get-neighbors-query "MATCH (graph:Graph)-[:HAS_NODE]->(node)-[:CONNECT]->(neighbors)
                          WHERE graph.name = {graphName} AND node.id = {nodeId}
                          SET node.visited = true
                          RETURN node, FILTER(n IN COLLECT(neighbors) WHERE n.visited = false) as neighbors;")

(def clean-visited-flags-query "MATCH (graph:Graph)-[:HAS_NODE]->(node)
                                WHERE graph.name = {graphName}
                                SET node.visited = false;")

(defn get-neighbors
  [graphName nodeId]
  (first (cy/tquery neo4j/conn get-neighbors-query {:graphName graphName :nodeId nodeId})))

(defn clean-visited-flags
  [graphName]
  (cy/tquery neo4j/conn clean-visited-flags-query {:graphName graphName}))
