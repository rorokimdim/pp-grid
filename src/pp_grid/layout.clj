(ns pp-grid.layout
  (:require [pp-grid.core :as c]))

(defn valign
  ([grids]
   (valign grids 0 0))
  ([grids x-padding y-padding]
   (valign grids x-padding y-padding false))
  ([grids x-padding y-padding center?]
   (let [grids (remove nil? grids)]
     (if (<= (count grids) 1)
       (first grids)
       (let [[top bottom] (take 2 grids)
             x-diff (- (:width top) (:width bottom))
             dx (+ x-padding (if center? (max (quot x-diff 2) 0) 0))
             dy (+ y-padding (:height top))]
         (valign
          (cons
           (c/add top (c/transform bottom (c/tf-translate dx dy)))
           (drop 2 grids))
          x-padding
          y-padding))))))

(defn halign
  ([grids]
   (halign grids 0 0))
  ([grids x-padding y-padding]
   (halign grids x-padding y-padding false))
  ([grids x-padding y-padding center?]
   (let [grids (remove nil? grids)]
     (if (<= (count grids) 1)
       (first grids)
       (let [[left right] (take 2 grids)
             dx (+ x-padding (:width left))
             y-diff (- (:height left) (:height right))
             dy (+ y-padding (if center? (max (quot y-diff 2) 0) 0))]
         (halign
          (cons
           (c/add left (c/transform right (c/tf-translate dx dy)))
           (drop 2 grids))
          x-padding
          y-padding
          center?))))))

(defn hspacer
  ([n]
   (if (<= n 0) (c/empty-grid)
       (-> (c/empty-grid)
           (assoc [0 0] \space)
           (assoc [(dec n) 0] \space)))))

(defn vspacer
  ([n]
   (if (<= n 0) (c/empty-grid)
       (-> (c/empty-grid)
           (assoc [0 0] \space)
           (assoc [0 (dec n)] \space)))))

(defn spacer
  ([width height]
   (c/add (hspacer width) (vspacer height))))

(defn === [x-padding & grids]
  (halign grids x-padding 0 true))

(defn || [y-padding & grids]
  (valign grids 0 y-padding true))
