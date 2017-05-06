(ns sybil_graph.models.utils
  (:require [clojurewerkz.neocons.rest        :as nr]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [sybil_graph.models.neo4j         :as neo4j]))

(def get-nodes-query "MATCH (graph:Graph)-[:HAS_NODE]->(node:Node)
                      WHERE graph.name = {graphName}
                      RETURN node
                      ORDER BY node.trust ASC
                      LIMIT {limit}")

(defn get-nodes
  [graphName limit]
  (cy/tquery neo4j/conn get-nodes-query {:graphName graphName
                                         :limit limit }))
