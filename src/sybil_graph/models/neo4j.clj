(ns sybil_graph.models.neo4j
  (:require [clojurewerkz.neocons.rest        :as nr]))

(def conn (nr/connect (or (System/getenv "GRAPHENEDB_URL") "http://localhost:7474/db/data/")))
