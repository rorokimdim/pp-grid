(ns pp-grid.layout
  (:require [pp-grid.core :as c]))

(defn valign
  "Constructs a grid containing given grids aligned vertically.

  For example, (valign [(text \"A\") (text \"B\") (text \"C\")]) is
  A
  B
  C
  "
  ([grids]
   (valign grids 0 0))
  ([grids x-padding y-padding]
   (valign grids x-padding y-padding false))
  ([grids x-padding y-padding center?]
   (let [grids (remove nil? grids)]
     (if (<= (count grids) 1)
       (first grids)
       (let [[top bottom] (take 2 grids)
             top-width (:width top)
             bottom-width (:width bottom)
             top-center-x (+ (:min-x top) (/ top-width 2))
             bottom-center-x (+ (:min-x bottom) (/ bottom-width 2))
             x-diff (- top-center-x bottom-center-x)
             dx (+ x-padding (if center? x-diff 0))
             dy (+ y-padding (:height top))]
         (valign
          (cons
           (c/add top (c/transform bottom (c/tf-translate dx dy)))
           (drop 2 grids))
          x-padding
          y-padding
          center?))))))

(defn halign
  "Constructs a grid containing given grids aligned horizontally.

  For example, (halign [(text \"A\") (text \"B\") (text \"C\")]) is ABC."
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
             left-height (:height left)
             right-height (:height right)
             left-center-y (+ (:min-y left) (/ left-height 2))
             right-center-y (+ (:min-y right) (/ right-height 2))
             y-diff (- left-center-y right-center-y)
             dy (+ y-padding (if center? y-diff 0))]
         (halign
          (cons
           (c/add left (c/transform right (c/tf-translate dx dy)))
           (drop 2 grids))
          x-padding
          y-padding
          center?))))))

(defn hspacer
  "Constructs a horizontal space of given length.

  For example, (hspacer 5) is \"     \"."
  ([n]
   (if (<= n 0) (c/empty-grid)
       (-> (c/empty-grid)
           (assoc [0 0] \space)
           (assoc [(dec n) 0] \space)))))

(defn vspacer
  "Constructs a vertical space of given length.

  For example, (vspacer 5) is
  (space)
  (space)
  (space)
  (space)
  (space)
  "
  ([n]
   (if (<= n 0) (c/empty-grid)
       (-> (c/empty-grid)
           (assoc [0 0] \space)
           (assoc [0 (dec n)] \space)))))

(defn spacer
  "Constructs a rectangular space of given width and height."
  ([width height]
   (c/add (hspacer width) (vspacer height))))

(defn ===
  "Horizontally aligns given grids with some defaults.

  Just a convenience wrapper for halign to accept grids as args and
  use some default values for padding and centering."
  [x-padding & grids]
  (halign grids x-padding 0 true))

(defn ||
  "Vertically aligns given grids with some defaults.

  Just a convenience wrapper for valign to accept grids as args and
  use some default values for padding and centering."
  [y-padding & grids]
  (valign grids 0 y-padding true))

(defn pull
  "Pulls a grid by given amounts horizontally and vertically.

  Just a convenience function for transforming with tf-translate. Useful
  for tweaking alignments."
  [g dx dy]
  (c/transform g (c/tf-translate dx dy)))
