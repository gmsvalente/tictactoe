(ns tictactoe.events
  (:require [re-frame.core :as rf]))

;; cycle through turns
(def turns (cycle [\X \O]))

;; new game
(def new-game {:next \X
               :winner? nil
               :counter 0
               :cells {}})

;; initial-db
(rf/reg-event-db
 :init-db
 (fn [db _]
   {:game new-game
    :hall-of-fame {\X 0
                   \O 0}}))

;; reset game event
(rf/reg-event-db
 :reset-game
 (fn [db _]
   (assoc db :game new-game)))

;; increment game counter
(rf/reg-event-db
 :inc-counter
 (fn [db _]
   (update-in db [:game :counter] inc)))

;; change next player move
(rf/reg-event-db
 :next-move
 (fn [{:keys [game] :as db} _]
   (assoc-in db [:game :next] (nth turns (:counter game)))))


(def winner-cells
  [#{1 2 3} #{4 5 6} #{7 8 9} ; rows
   #{1 4 7} #{2 5 8} #{3 6 9} ; columns
   #{1 5 9} #{3 5 7} ;; diags
   ])

(defn transform-cells
  "This function transforms the game cell {1 \\x 3 \\x 4 \\o} to {\\o #{4} \\x #{1 3}}"
  [cells]
  (reduce (fn [acc [k v]]
            (update acc v (fnil conj #{}) k))
          {}
          cells))

(defn key-winner?
  "Checks if the transformed-cells has a winner.
  If any player has in their history a winner cell return true o.w. false"
  [m]
  (update-keys m (fn [k]
                   (some true? (map #(clojure.set/subset? % k) winner-cells)))))

(defn check-winner
  "Check if we got a winner"
  [{:keys [effects] :as ctx}]
  (let [winner? (-> effects    
                    :db        
                    :game
                    :cells
                    transform-cells
                    clojure.set/map-invert
                    key-winner?
                    (get true))]
    (if winner?
      (-> ctx
          (assoc-in [:effects :db :game :winner?] winner?)
          (update-in [:effects :db :hall-of-fame winner?] inc))
      ctx)))


(def winner?
  "This interceptor checks for a winner"
  (rf/->interceptor
   :id :check-winner
   :after (fn [ctx]
            (check-winner ctx))))

;; this events add the cell id to game cells, increment counter and get next player
(rf/reg-event-fx
 :cell-click
 [winner?]
 (fn [{:keys [db]} [_ cell-number]]
   {:db (assoc-in db [:game :cells cell-number] (-> db :game :next))
    :fx [[:dispatch [:inc-counter]]
         [:dispatch [:next-move]]]}))
