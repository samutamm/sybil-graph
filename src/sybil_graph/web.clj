(ns sybil_graph.web
  (:require [clojurewerkz.neocons.rest        :as nr]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [compojure.core                   :refer [GET POST defroutes]]
            [compojure.route                  :as route]
            [compojure.handler                :as handler]
            [ring.adapter.jetty               :as ring]
            [ring.util.response               :as resp]
            [ring.middleware.json             :as rj]
            [ring.middleware.cors             :refer [wrap-cors]]
            [compojure.route                  :as route]
            [sybil_graph.layouts.layout       :as layout]
            [sybil_graph.controllers.content  :as content]
            [sybil_graph.controllers.randomwalk  :as randomwalk])
            (:gen-class))

(defroutes api-and-site-routes
  (POST "/graphs/new" [name nodes sybils attackedges]
    (do  (content/create-graph name
          (Integer/parseInt nodes)
          (Integer/parseInt sybils)
          (Integer/parseInt attackedges))(resp/redirect "/graphs")))
  (GET "/" [] (layout/form "Home"))
  (GET "/graphs" [] (layout/graphs-view "Graphs" (content/get-graphs)))
  (GET "/randomWalks/:id" [id] (layout/randomwalk-results "Random walk results"))
  (POST "/delete/:id" [id] (do (content/remove-graph id) (resp/redirect "/graphs") ))
  (POST "/randomWalk/:graphId" [graphId seeds stepfactor nodes graphName]
    (do
      (randomwalk/simulate-randomwalk {:graphId graphId :seeds seeds :stepfactor stepfactor
                                       :nodes nodes :graphName graphName})
      (resp/redirect (str "/randomWalks/" graphId) )))
  (route/resources "/")
  (route/not-found "Not Found"))

(def application
  (-> api-and-site-routes
    (handler/api)
    (rj/wrap-json-response)
    (rj/wrap-json-body)))

(defn start [port]
 (ring/run-jetty application {:port port
                              :join? false}))

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))
