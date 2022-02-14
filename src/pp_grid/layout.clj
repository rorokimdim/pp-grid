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
