(ns tictactoe.tiles)

(def tiles
  {\O  [:svg {:xmlns "http://www.w3.org/2000/svg"
              :viewBox "0 0 100 100"
              :class "w-12 h-12"}
        [:circle
         {:cx "50"
          :cy "50"
          :r "40"
          :stroke "black"
          :stroke-width "5"
          :fill "none"}]]
   \X [:svg {:xmlns "http://www.w3.org/2000/svg"
             :viewBox "0 0 100 100"
             :class "w-12 h-12"}
       [:line
        {:x1 "10"
         :y1 "10"
         :x2 "90"
         :y2 "90"
         :stroke "black"
         :stroke-width "5"}]
       [:line
        {:x1 "90"
         :y1 "10"
         :x2 "10"
         :y2 "90"
         :stroke "black"
         :stroke-width "5"}]]})
