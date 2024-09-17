(ns tictactoe.game
  (:require [reagent.dom.client :as client]))

(defonce root (client/create-root (.getElementById js/document "root")))

(defn game
  []
  [:div
   [:h1 "TICTACTOE"]])


(defn ^:dev/load-after render!
  []
  (client/render root [game]))

(defn -main
  []
  (println "TICTACTOE")
  (render!))
