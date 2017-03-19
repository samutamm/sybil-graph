(ns sybil_graph.models.sybils
  (:require [clojurewerkz.neocons.rest        :as nr]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [sybil_graph.models.neo4j         :as neo4j]))

(def create-graph-query "CREATE (graph:Graph {name: {name}}) RETURN graph;")

(def get-all-graphs-query "MATCH (graph:Graph) RETURN { id: ID(graph), node: graph };")

(def add-sybils-query "MATCH (graph:Graph)
                          WHERE graph.name = {graphName}
                          CREATE (node:NormalNode {id: 1}),
                          (org)-[:HAS_CHANNEL]->(c)")

(defn create-graph
  [name]
  (let [params {:name (str name)}]
    (cy/tquery neo4j/conn create-graph-query params)))

(defn get-all-graphs
  []
  (cy/tquery neo4j/conn get-all-graphs-query {}))

(defn add-sybils
  []
  (let [params {:organization "asda"}]
    (cy/tquery neo4j/conn add-sybils-query params)))
