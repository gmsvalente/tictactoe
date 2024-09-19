(ns tictactoe.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :cell-value
 (fn [db [_ id]]
   (get (-> db :game :cells) id \_)))

(rf/reg-sub
 :tie
 (fn [db _]
   (> (-> db :game :counter) 8)))

(rf/reg-sub
 :winner
 (fn [db _]
   (-> db :game :winner?)))

(rf/reg-sub
 :hall-of-fame
 (fn [db]
   (db :hall-of-fame)))

(rf/reg-sub
 :cell-disabled
 (fn [db [_ id]]
   ((-> db :game :cells) id)))
