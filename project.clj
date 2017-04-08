(defproject sybil-graph "0.1.0-SNAPSHOT"
  :description "Sybil Graph experiments"
  :min-lein-version "2.7.1"
  :uberjar-name "sybil-standalone.jar"
  :repositories [["jitpack" "https://jitpack.io"]]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojurewerkz/neocons "3.1.0"]
                 [org.immutant/web "2.0.0-beta2"]
                 [compojure "1.5.1"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [clojurewerkz/neocons "3.1.0"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-devel "1.6.0-RC1"]
                 [ring-cors "0.1.6"]
                 [environ "1.0.0"]
                 [hiccup "1.0.5"]
                 [org.clojure/tools.logging "0.3.1"]
                 [incanter "1.5.7"]
                 [com.github.Data2Semantics/powerlaws "v0.1.0"]]
  :main ^:skip-aot sybil_graph.web
  :plugins [[lein-ring "0.8.11"]
            [lein-immutant "2.0.0-alpha2"]]
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring-mock "0.1.5"]]}
             :uberjar {:aot :all}})
