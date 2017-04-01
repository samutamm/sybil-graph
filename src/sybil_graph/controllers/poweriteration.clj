(ns sybil_graph.controllers.poweriteration
 (:require [sybil_graph.models.poweriteration    :as power]))

;(initialize-seeds {:seeds 3 :lastNormalNodeId 100 :graphName "kalle" :initTrust 324})
(defn initialize-seeds
  [params]
  (loop [i 0]
    (if (< i (:seeds params))
      (let [randomId (inc (rand-int (:lastNormalNodeId params)))]
        (do
          (power/add-trust-to-node (:graphName params) randomId (:initTrust params) 0)
          (recur (inc i)))))))

(defn poweriteration
  "Iterate all nodes and update their temporary trust. Then replace temp trust by trust."
  [params]
  (do
    (let [firstId 1
          lastId 110]
      (loop [id firstId]
        (if (<= id lastId)
          (let [neighbors (power/get-neighbors (:graphName params) id)
                trust (get-in (first neighbors) ["node" :data :trust])]
            (do
              (if (> trust 0)
                (power/add-temptrust-to-neighbors (:graphName params) id (/ trust (count neighbors)))))
            (recur (inc id))))))
    (power/set-temptrust-to-trust (:graphName params))))

(defn iterate-log
  [params]
  (do
    (initialize-seeds
      {:seeds (:seeds params) :lastNormalNodeId 100 :graphName (:graphName params) :initTrust 648})
    (let [iterations (Math/round (Math/log 100))]
      (loop [i 0]
      (if (< i iterations)
        (do
          (println (str "ITERATION: " i))
          (poweriteration {:graphName (:graphName params)})
          (recur (inc i))))))))
