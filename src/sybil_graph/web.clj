(ns sybil_graph.web
  (:require [clojurewerkz.neocons.rest        :as nr]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [compojure.core                   :refer [GET POST defroutes]]
            [compojure.route                  :as route]
            [compojure.handler                :as handler]
            [ring.adapter.jetty               :as ring]
            [ring.util.response               :as resp]
            [ring.middleware.json             :as rj]
            [ring.middleware.reload           :refer [wrap-reload]]
            [ring.middleware.resource         :refer [wrap-resource]]
            [compojure.route                  :as route]
            [sybil_graph.layouts.layout       :as layout]
            [sybil_graph.controllers.content  :as content]
            [sybil_graph.controllers.randomwalk  :as randomwalk]
            [sybil_graph.controllers.poweriteration  :as power])
            (:gen-class))

(defroutes api-and-site-routes
  (POST "/graphs/new" [name nodes sybils attackedges]
    (do  (content/create-graph name
          (Integer/parseInt nodes)
          (Integer/parseInt sybils)
          (Integer/parseInt attackedges))(resp/redirect "/graphs")))
  (GET "/" [] (layout/form "Home"))
  (GET "/graphs" [] (layout/graphs-view "Graphs" (content/get-graphs)))
  (GET "/randomWalks/:id" [id]
    (layout/randomwalk-results "Random walk results" (randomwalk/get-randomwalk id)))
  (GET "/poweriteration/:id" [id]
    (layout/poweriteration-results "poweriteration results" nil)) ;TODO resultpage
  (POST "/delete/:id" [id] (do (content/remove-graph id) (resp/redirect "/graphs") ))
  (POST "/randomWalk/:graphId" [graphId seeds stepfactor nodes graphName]
    (let [randomwalkId (randomwalk/do-randomwalk {:graphId (Integer/parseInt graphId) :seeds seeds :stepfactor stepfactor
                                                :nodes nodes :graphName graphName})]
    (resp/redirect (str "/randomWalks/" randomwalkId))))
  (POST "/poweriteration/:id" [id seeds graphName]
    (let [iterations (Math/round (Math/log 100))
          poweriterationId (power/start-iterations
            {:iterations iterations :graphName graphName :seeds (Integer/parseInt seeds)})]
    (resp/redirect (str "/poweriteration/19")))) ;TODO add correct id
  (route/resources "/")
  (route/not-found "Not Found"))

(def application
  (-> api-and-site-routes
    (wrap-reload)
    (wrap-resource "public")
    (handler/api)
    (rj/wrap-json-response)
    (rj/wrap-json-body)))

(defn start [port]
 (ring/run-jetty application {:port port
                              :join? false}))

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))
