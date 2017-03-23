(ns sybil_graph.models.sybils
  (:require [clojurewerkz.neocons.rest        :as nr]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [sybil_graph.models.neo4j         :as neo4j]))

(def create-graph-query "CREATE (graph:Graph {name: {name}}) RETURN graph;")

(def get-all-graphs-query "MATCH (graph:Graph) RETURN graph;")

(def remove-graph-query "MATCH (g:Graph) where ID(g) = {id}
                        OPTIONAL MATCH (g)-[r1]-(n)-[r2]-()
                        DELETE r2,r1,g,n;")

(def add-node-to-graph-query "MATCH (g:Graph)
                              WHERE g.name = {graphName}
                              CREATE (n:Node {id: {id}, sybil: {isSybil}}),
                              (g)-[:HAS_NODE]->(n),
                              (n)-[:BELONGS_TO]->(g);")

(def create-relations-query "MATCH (g:Graph)-[:HAS_NODE]->(n), (g:Graph)-[:HAS_NODE]->(otherNode)
                            WHERE g.name = {graphName}
                            AND n.id = {nodeId}
                            AND otherNode.id IN {otherIds}
                            CREATE UNIQUE (n)-[:CONNECT]->(otherNode), (otherNode)-[:CONNECT]->(n);")

(defn create-graph
  [name]
  (let [params {:name (str name)}]
    (cy/tquery neo4j/conn create-graph-query params)))

(defn get-all-graphs
  []
  (cy/tquery neo4j/conn get-all-graphs-query {}))

(defn remove-graph
  [id]
  (let [params {:id (Integer/parseInt id)}]
    (cy/tquery neo4j/conn remove-graph-query params)))

(defn add-node-to-graph
  [name id isSybil]
  (let [params {:graphName name
                :id id
                :isSybil isSybil}]
  (cy/tquery neo4j/conn add-node-to-graph-query params)))

(defn create-relations
  [name nodeId otherIds]
  (cy/tquery neo4j/conn create-relations-query {:graphName name
                                                 :nodeId nodeId
                                                 :otherIds otherIds}))
