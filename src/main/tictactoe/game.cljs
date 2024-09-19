(ns tictactoe.game
  (:require [clojure.string :as str]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [reagent.dom.client :as client]
            [tictactoe.events]
            [tictactoe.subs :as sub]
            [tictactoe.tiles :refer [tiles]]))

;; div root
(defonce root (client/create-root (.getElementById js/document "root")))

(defn hall-of-fame
  "The score of the game"
  []
  (let [hof (rf/subscribe [:hall-of-fame])]
    [:div {:class "flex flex-row justify-around items-center h-80"}
     [:div {:class "h-full flex flex-col justify-evenly"}
      [:div
       (tiles "X")]
      [:div {:class "flex justify-center"}
       [:h1 {:class "h-full text-3xl"} (@hof "X")]]]
     [:div [:h1 {:class "text-xl"} "VS"]]
     [:div {:class "h-full flex flex-col justify-evenly"}
      [:div
       (tiles "O")]
      [:div {:class "flex justify-center"}
       [:h1 {:class "h-full text-3xl"} (@hof "O")]]]]))

(defn winner
  "The winner banner"
  [winner]
  [:div {:class "h-80 w-80  flex flex-col justify-between"}
   [:h1 {:class "text-4xl"}
    (if winner
      (str "The winner is " winner " !")
      "This is a Tie !")]
   [hall-of-fame]])

(defn modal
  "This modal will pop when tie or winner!"
  [result]
  [:div {:class "relative z-10"}
   [:div {:class "fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" :aria-hidden true}]
   [:div {:class "fixed inset-0 z-10 w-screen overflow-y-auto"}
    [:div {:class "flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0"}
     [:div {:class "relative transform overflow-hidden rounded-lg bg-white text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-lg"}
      [:div {:class "bg-white px-4 pb-4 pt-5 sm:p-6 sm:pb-4"}
       [:div {:class "sm:flex sm:items-start"}
        (if result 
          [winner result]
          [winner nil])
        [:button {:class "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                  :on-click #(rf/dispatch [:reset-game])}
         "Continue"]]]]]]])


(defn modal-result
  "Checks the result of the game and display modal if game-over"
  []
  (let [winner? (rf/subscribe [:winner])
        tie? (rf/subscribe [:tie])]
    (cond
      @tie? [modal nil]
      @winner? [modal @winner?])))

(defn cell
  "The cells of tictactoe"
  [id class]
  (let [v (rf/subscribe [:cell-value id])
        disabled? (rf/subscribe [:cell-disabled id])]
    (fn []
      [:div
       [:div {:id id
              :class (str/join " " ["w-full h-full bg-blue-100 flex items-center justify-center" (when @disabled? "disabled cursor-not-allowed")])
              :disabled @disabled?
              :on-click #(when-not @disabled?
                           (rf/dispatch [:cell-click id]))}
        [:div {:class "h-12"}
         (tiles @v)]]])))

(defn board
  "The TTT board"
  []
  [:div {:class "bg-black shadow-lg rounded mt-60 w-90 h-90"}
   [:div {:class "grid grid-cols-3 gap-4 w-80 h-80"}
    (for [i (range 1 10)]
      ^{:key i} [cell i])]])

(defn title-bar
  "The title bar"
  []
  [:div {:class "bg-blue-500 justify-center w-full h-20 shadow-lg"}
   [:h1 {:class "h-full font-bold flex flex-col justify-center items-center font-mono text-2xl text-white"} "TicTacToe"]])

(defn game
  "The game itself"
  []
  (fn []
    [:div {:class "flex flex-col items-center h-screen w-full bg-blue-300"}
     [title-bar]
     [modal-result]
     [board]]))


(defn ^:dev/after-load render!
  "Reloader"
  []
  (client/render root [game]))

(defn -main
  []
  (println "TICTACTOE")
  (rf/dispatch-sync [:init-db]) ;; generate the re-frame app db
  (render!))
