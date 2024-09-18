(ns tictactoe.game
  (:require [reagent.dom.client :as client]))

(defonce root (client/create-root (.getElementById js/document "root")))

(defn cell-click
  [cell-no]
  (fn [el]
    (println "cliked somewhere> " cell-no)
    (println "cell-content= " (.-innerHTML (.-target el)))))


(defn board
  []
  [:div.board
   [:div.row
    [:div.cell {:on-click (cell-click 1)} (str "X") ]
    [:div.cell {:on-click (cell-click 2)} (str "O") ]
    [:div.cell {:on-click (cell-click 3)} (str "X") ]]
   [:div.row
    [:div.cell {:on-click (cell-click 4)} (str "X") ]
    [:div.cell {:on-click (cell-click 5)} (str "O") ]
    [:div.cell {:on-click (cell-click 6)} (str "X") ]]
   [:div.row
    [:div.cell {:on-click (cell-click 7)} (str "X") ]
    [:div.cell {:on-click (cell-click 8)} (str "O") ]
    [:div.cell {:on-click (cell-click 9)} (str "X") ]]])

(defn title-bar
  []
  [:div
   [:h1 "TicTacToe"]])

(defn game
  []
  [:<>
   [title-bar]
   [board]])


(defn ^:dev/after-load render!
  []
  (client/render root [game]))

(defn -main
  []
  (println "TICTACTOE")
  (render!))
