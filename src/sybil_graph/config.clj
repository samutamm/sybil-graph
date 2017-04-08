(ns sybil_graph.config)

(def small
  {
    :firstId 1
    :lastId 1000
    :sybilFirst 1001
    :sybilLast 1100
    :initTrust 1248
    :normalDistMean 10
    :normalDistSD 3
    :powerlaw 2
    :maxDegree 200
    })

(def giant
  {
    :firstId 1
    :lastId 10000
    :sybilFirst 10001
    :sybilLast 11000
    :initTrust 2496
    :normalDistMean 10
    :normalDistSD 3
    :powerlaw 2
    :maxDegree 200
    })

(def default-config
  small)
