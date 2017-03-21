(defproject sybil-graph "0.1.0-SNAPSHOT"
  :description "Sybil Graph experiments"
  :min-lein-version "2.7.1"
  :uberjar-name "sybil-standalone.jar"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojurewerkz/neocons "3.1.0"]
                 [org.immutant/web "2.0.0-beta2"]
                 [compojure "1.5.1"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [clojurewerkz/neocons "3.1.0"]
                 [ring/ring-json "0.3.1"]
                 [ring-cors "0.1.6"]
                 [environ "1.0.0"]
                 [hiccup "1.0.5"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-log4j12 "1.7.1"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]]
  :main ^:skip-aot sybil_graph.web
  :plugins [[lein-ring "0.8.11"]
            [lein-immutant "2.0.0-alpha2"]]
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring-mock "0.1.5"]]}
             :uberjar {:aot :all}})
