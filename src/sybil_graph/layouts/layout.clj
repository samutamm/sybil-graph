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
           (text-field "name")
           (submit-button {:name "submit"} "Create"))]]]))

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
