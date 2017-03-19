(ns sybil_graph.layouts.layout
 (:require [hiccup.page :refer [html5 include-css include-js]]
           [hiccup.form :refer [form-to, text-field, submit-button]]))

(defn form
  [title & content]
  (html5
    {:lang "en"}
    [:head
      [:title title]
      [:body
        [:div
          [:h1 "Hiccup"]
          (form-to {:enctype "application/x-www-form-urlencoded"}
            [:post "/graphs/new"]
           (text-field "name")
           (submit-button {:class "btn" :name "submit"} "Create"))]]]))


(defn graphs-view
  [title graphs]
  (html5
    {:lang "en"}
    [:head
      [:title title]
      [:body
        [:ul
         (for [g graphs]
           [:li (str "ID: "(:id g) ", NAME: " (:name (:node g))
                  "ID: "(get g "id") ", NAME: " (get-in g ["name" "node"] ) "------" g) ])]]]))
