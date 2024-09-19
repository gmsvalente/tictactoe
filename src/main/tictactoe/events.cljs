(ns tictactoe.events
  (:require [re-frame.core :as rf]))

(def turns (cycle [\X \O]))

(def new-game {:next \X
               :winner? nil
               :counter 0
               :cells {}})

(rf/reg-event-db
 :init-db
 (fn [db _]
   {:users {}
    :game new-game
    :hall-of-fame {}}))

(rf/reg-event-db
 :inc-counter
 (fn [db _]
   (update-in db [:game :counter] inc)))

(rf/reg-event-db
 :next-move
 (fn [{:keys [game] :as db} _]
   (assoc-in db [:game :next] (nth turns (:counter game)))))

(defn transform-cells
  [cells]
  (reduce (fn [acc [k v]]
            (update acc v (fnil conj #{}) k))
          {}
          cells))

(def winner-cells
  [#{1 2 3} #{4 5 6} #{7 8 9} ; rows
   #{1 4 7} #{2 5 8} #{3 6 9} ; columns
   #{1 5 9} #{3 5 7} ;; diags
   ])

(defn key-winner?
  [m]
  (update-keys m (fn [k]
                   (some true? (map #(clojure.set/subset? % k) winner-cells)))))

(defn check-winner
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
      (assoc-in ctx [:effects :db :game :winner?] winner?)
      ctx)))

(def winner?
  (rf/->interceptor
   :id :check-winner
   :after (fn [ctx]
            (check-winner ctx))))

(rf/reg-event-fx
 :cell-click
 [winner?]
 (fn [{:keys [db]} [_ cell-number]]
   {:db (assoc-in db [:game :cells cell-number] (-> db :game :next))
    :fx [[:dispatch [:inc-counter]]
         [:dispatch [:next-move]]]}))

