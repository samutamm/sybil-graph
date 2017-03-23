(ns sybil_graph.layouts.layout
 (:require [hiccup.page :refer [html5 include-css include-js]]
           [hiccup.form :refer [form-to, text-field, submit-button]]
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
           (text-field {:type "number" :min 1 :max 20 } "nodes")
           [:p "Max number of relations for sybil nodes."]
           (text-field {:type "number" :min 1 :max 9 :id "sybils"} "sybils")
           [:p "Attack edges."]
           (text-field {:type "number" :min 1 :max 9 :id "sybils"} "attackedges")
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
      (include-js "js/script.js")
      [:body
        (link-to "https://github.com/samutamm/sybil-graph" "Github")
        [:div
         (for [g graphs]
           [:div {:class "graph-div"}
            [:p (str "ID: "(:id g) ", NAME: " (get-in g [:data :name])) ]
            [:span (button (str "/delete/" (:id g)) "Delete")]])]]]))
