(ns sybil_graph.controllers.poweriteration
 (:require
   [sybil_graph.models.poweriteration    :as power]
   [sybil_graph.models.utils             :as utils]
   [sybil_graph.config                   :as config]))

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
    (let [firstId (:firstId config/default-config)
          lastId (:sybilLast config/default-config)]
      (loop [id firstId]
        (if (<= id lastId)
          (let [neighbors (power/get-neighbors (:graphName params) id)
                trust (get-in (first neighbors) ["node" :data :trust])]
            (do
              (if (> trust 0)
                (power/add-temptrust-to-neighbors (:graphName params) id (/ trust (count neighbors)))))
            (recur (inc id))))))
    (power/set-temptrust-to-trust (:graphName params))))

(defn start-iterations
  [params]
  (do
    (initialize-seeds
      {:seeds (:seeds params) :lastNormalNodeId (:lastId config/default-config)
        :graphName (:graphName params) :initTrust (:initTrust config/default-config)})
    (let [iterations (:iterations params)]
      (loop [i 0]
        (if (< i iterations)
          (do
            (println (str "ITERATION: " i))
            (poweriteration {:graphName (:graphName params)})
            (recur (inc i))))))))

(defn count-results
  [graphName limit]
  (count
    (filter
      (fn[x] (:sybil x))
      (map (fn[x] (get-in x ["node" :data])) (utils/get-nodes graphName limit)))))
