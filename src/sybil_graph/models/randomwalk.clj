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

(def save-randomwalk-query "MATCH (graph:Graph) WHERE ID(graph) = 1447
                            CREATE (rw:RandomWalk {sybils: {sybils}, impasses: {impasses},
                            sybilPercent: {sybilPercent}, impassePercent: {impassePercent}}),
                            (graph)-[:HAS_RANDOMWALK]->(rw) RETURN rw as randomwalk;")

(def get-randomwalk-query "MATCH (rw:RandomWalk) WHERE ID(rw) = {randomwalkId} RETURN rw as randomwalk;")

(defn get-neighbors
  [graphName nodeId]
  (first (cy/tquery neo4j/conn get-neighbors-query {:graphName graphName :nodeId nodeId})))

(defn clean-visited-flags
  [graphName]
  (cy/tquery neo4j/conn clean-visited-flags-query {:graphName graphName}))

(defn save-randomwalk
  [params]
  (first (cy/tquery neo4j/conn save-randomwalk-query params)))

(defn get-randomwalk-by-id
  [id]
  (first (cy/tquery neo4j/conn get-randomwalk-query {:randomwalkId id})))
