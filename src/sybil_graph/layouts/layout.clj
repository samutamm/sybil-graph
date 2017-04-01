(ns sybil_graph.layouts.layout
 (:require [hiccup.page :refer [html5 include-css include-js]]
           [hiccup.form :refer [form-to, text-field, submit-button, hidden-field]]
           [hiccup.element :refer [link-to]]))

(defn form
  [title & content]
  (html5 {:lang "en"}
    [:head
      [:title title]
      (include-css "styles.css")
      (include-js "js/script.js")
      [:body
        (link-to "https://github.com/samutamm/sybil-graph" "Github")
        [:div
          [:h1 "Sybil-graph simulator"]
          (form-to {:enctype "application/x-www-form-urlencoded"}
            [:post "/graphs/new"]
           [:p "Graph name"]
           (text-field "name")
           [:p "Max number of relations for normal nodes."]
           (text-field {:type "number" :min 1 :max 50 } "nodes")
           [:p "Max number of relations for sybil nodes."]
           (text-field {:type "number" :min 1 :max 50 :id "sybils"} "sybils")
           [:p "Attack edges."]
           (text-field {:type "number" :min 1 :max 50 :id "sybils"} "attackedges")
           (submit-button {:name "submit" :id "submit-button"} "Create"))]]]))

(defn button
  [path text]
  (form-to
    [:post path]
   (submit-button {:name "submit"} text)))

(defn graphs-view
  [title graphs]
  (html5
    {:lang "en"}
    [:head
      [:title title]
      (include-css "styles.css")
      (include-js "https://code.jquery.com/jquery-3.2.1.slim.min.js")
      (include-js "js/script.js")
      [:body {:onLoad "addListeners();"}
        (link-to "https://github.com/samutamm/sybil-graph" "Github")
        [:div
         (for [g graphs]
           [:div {:class "graph-div"}
            [:p (str "ID: "(:id g) ", NAME: " (get-in g [:data :name])) ]
            [:div {:class "experiments"}
              [:h1 "Randomwalk"]
              (form-to {:enctype "application/x-www-form-urlencoded"}
                [:post (str "/randomWalk/" (:id g))]
                  [:p "Try with how many seeds"]
                  (text-field "seeds")
                  [:p "How many steps? Write log or give an integer for factor. (log n, 1*n, 2*n...)"]
                  (text-field "stepfactor")
                  (hidden-field "nodes" (:nodes g))
                  (hidden-field "graphName" (get-in g [:data :name]))
                (submit-button {:name "submit"} "Test random walk"))]
              [:div {:class "experiments"}
                [:h1 "Poweriteration"]
                (form-to {:enctype "application/x-www-form-urlencoded"}
                  [:post (str "/poweriteration/" (:id g))]
                    [:p "How many seeds"]
                    (text-field "seeds")
                    (hidden-field "graphName" (get-in g [:data :name]))
                  (submit-button {:name "submit"} "Start poweriteration"))]
            [:p ""]
            [:span (button (str "/delete/" (:id g)) "Delete")]])]]]))

(defn randomwalk-results
  [title randomwalk]
  (html5 {:lang "en"}
    [:head
      [:title title]
      (include-css "styles.css")
      (include-js "js/script.js")
      [:body
        [:p "Random walks results" ]
        [:p (str "Sybils: " (:sybils randomwalk))]
        [:p (str "impasses: "(:impasses randomwalk))]
        [:p (str "sybilpercent: "(:sybilPercent randomwalk))]]]))

(defn poweriteration-results
  [title poweriteration]
  (html5 {:lang "en"}
    [:head
      [:title title]
      (include-css "styles.css")
      (include-js "js/script.js")
      [:body
        [:p "Poweriteration results" ]]]))
