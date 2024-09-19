(ns tictactoe.game
  (:require [clojure.string :as str]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [reagent.dom.client :as client]
            [tictactoe.events]
            [tictactoe.subs :as sub]))

(defonce root (client/create-root (.getElementById js/document "root")))

(defn cell
  [id]
  (let [v (rf/subscribe [:cell-value id])
        disabled? (r/atom false)]
    (fn []
      [:div {:id id
             :class (str/join " " ["cell" (when @disabled? "disabled")])
             :disabled @disabled?
             :on-click #(when-not @disabled?
                          (reset! disabled? true)
                          (rf/dispatch [:cell-click id]))}
       @v])))

(defn modal-result
  []
  (cond
    @(rf/subscribe [:tie]) [:h1 "tie"]
    @(rf/subscribe [:winner]) [:h1 "winner"]))

(defn board
  []
  [:div.board
   [modal-result]
   [:div.row
    [cell 1]
    [cell 2]
    [cell 3]]
   [:div.row
    [cell 4]
    [cell 5]
    [cell 6]]
   [:div.row
    [cell 7]
    [cell 8]
    [cell 9]]])

(defn title-bar
  []
  [:div
   [:h1 "TicTacToe"]])

(defn game
  []
  (fn []
    [:<>
     [:div
      [title-bar]
      [board]]]))


(defn ^:dev/after-load render!
  []
  (rf/dispatch-sync [:init-db])
  (client/render root [game]))

(defn -main
  []
  (println "TICTACTOE")
  (render!))
