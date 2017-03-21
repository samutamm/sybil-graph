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
            [clojure.tools.logging            :as log])
            (:gen-class))

(defroutes api-and-site-routes
  (POST "/graphs/new" [name] (do (content/create-graph name)(resp/redirect "/graphs")))
  (GET "/" [] (layout/form "Home"))
  (GET "/graphs" [] (layout/graphs-view "Graphs" (content/get-graphs)))
  (POST "/delete/:id" [id] (do (content/remove-graph id) (resp/redirect "/graphs") ))
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
