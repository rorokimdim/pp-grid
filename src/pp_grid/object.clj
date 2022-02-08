(ns pp-grid.object
  (:require [clojure.string :as s]

            [pp-grid.core :as c]
            [pp-grid.layout :as l]))

(defn text
  ([s]
   (text s 0 0))
  ([s pad-left pad-right]
   (text s pad-left pad-right \space))
  ([s pad-left pad-right pad-char]
   (->> s
        s/split-lines
        (map vector (range))
        (reduce
         (fn [g [y ^String line]]
           (reduce
            (fn [g [x v]]
              (assoc g [x y] v))
            g
            (map vector
                 (range)
                 (str (apply str (repeat pad-left pad-char))
                      (if (empty? line) " " line)
                      (apply str (repeat pad-right pad-char))))))
         (c/empty-grid)))))

(defn hline
  ([n]
   (hline n \─ \─ \─))
  ([n body-char]
   (hline n body-char body-char body-char))
  ([n body-char start-char end-char]
   (cond
     (<= n 0) nil
     (= n 1) (assoc (c/empty-grid) [0 0] body-char)
     (= n 2) (text (str start-char end-char))
     :else (text
            (s/join
             nil
             (list
              start-char
              (s/join nil (repeat (- n 2) body-char))
              end-char))))))

(defn vline
  ([n]
   (vline n \│ \│ \│))
  ([n body-char]
   (vline n body-char body-char body-char))
  ([n body-char start-char end-char]
   (cond
     (<= n 0) nil
     (= n 1) (assoc (c/empty-grid) [0 0] body-char)
     (= n 2) (text (str start-char \newline end-char))
     :else (text (s/join
                  \newline
                  (list
                   start-char
                   (s/join \newline (repeat (- n 2) body-char))
                   end-char))))))

(defn hfill
  ([n]
   (hfill n \space))
  ([n c]
   (hline n c)))

(defn vfill
  ([n]
   (vfill n \space))
  ([n c]
   (vline n c)))

(defn fill
  ([w h]
   (fill w h \space))
  ([w h c]
   (l/valign (repeat h (hfill w c)))))

(defn left-arrow
  ([n]
   (left-arrow n \─ "◀"))
  ([n body-char]
   (left-arrow n body-char "◀"))
  ([n body-char head-char]
   (hline n body-char head-char body-char)))

(defn right-arrow
  ([n]
   (right-arrow n \─ "▶︎"))
  ([n body-char]
   (right-arrow n body-char "▶︎"))
  ([n body-char head-char]
   (hline n body-char body-char head-char)))

(defn left-right-arrow
  ([n]
   (left-right-arrow n \─ "◀" "▶︎"))
  ([n body-char]
   (left-right-arrow n body-char "◀" "▶︎"))
  ([n body-char left-head-char right-head-char]
   (hline n body-char left-head-char right-head-char)))

(defn up-arrow
  ([n]
   (up-arrow n \│ "▲"))
  ([n body-char]
   (up-arrow n body-char "▲"))
  ([n body-char head-char]
   (vline n body-char head-char body-char)))

(defn down-arrow
  ([n]
   (down-arrow n \│ "▼"))
  ([n body-char]
   (down-arrow n body-char "▼"))
  ([n body-char head-char]
   (vline n body-char body-char head-char)))

(defn up-down-arrow
  ([n]
   (up-down-arrow n \│ "▲" "▼"))
  ([n body-char]
   (up-down-arrow n body-char "▲" "▼"))
  ([n body-char up-head-char down-head-char]
   (vline n body-char up-head-char down-head-char)))

(defn box
  [g & {:keys [left-padding
               right-padding
               top-padding
               bottom-padding
               left-border-char
               right-border-char
               top-border-char
               bottom-border-char
               top-left-corner-char
               top-right-corner-char
               bottom-left-corner-char
               bottom-right-corner-char]
        :or {left-padding 0
             right-padding 0
             top-padding 0
             bottom-padding 0
             left-border-char \|
             right-border-char \|
             top-border-char \-
             bottom-border-char \-
             top-left-corner-char \+
             top-right-corner-char \+
             bottom-left-corner-char \+
             bottom-right-corner-char \+}}]
  (let [width (+ (:width g) left-padding right-padding)
        height (+ (:height g) top-padding bottom-padding 2)]
    (l/halign
     [(vline height left-border-char top-left-corner-char bottom-left-corner-char)
      (l/valign [(hline width top-border-char)
                 (vfill top-padding \space)
                 (l/halign [(hfill left-padding) g (hfill right-padding)])
                 (vfill bottom-padding \space)
                 (hline width bottom-border-char)])
      (vline height right-border-char top-right-corner-char bottom-right-corner-char)])))

(defn box1 [g & {:keys [left-padding
                        right-padding
                        top-padding
                        bottom-padding
                        left-border-char
                        right-border-char
                        top-border-char
                        bottom-border-char
                        top-left-corner-char
                        top-right-corner-char
                        bottom-left-corner-char
                        bottom-right-corner-char]
                 :or {left-padding 0
                      right-padding 0
                      top-padding 0
                      bottom-padding 0
                      left-border-char "│"
                      right-border-char "│"
                      top-border-char "─"
                      bottom-border-char "─"
                      top-left-corner-char "┌"
                      top-right-corner-char "┐"
                      bottom-left-corner-char "└"
                      bottom-right-corner-char "┘"}}]
  (box g
       :left-padding left-padding
       :right-padding right-padding
       :top-padding top-padding
       :bottom-padding bottom-padding
       :left-border-char left-border-char
       :right-border-char right-border-char
       :top-border-char top-border-char
       :bottom-border-char bottom-border-char
       :top-left-corner-char top-left-corner-char
       :top-right-corner-char top-right-corner-char
       :bottom-left-corner-char bottom-left-corner-char
       :bottom-right-corner-char bottom-right-corner-char))

(defn box2 [g & {:keys [left-padding
                        right-padding
                        top-padding
                        bottom-padding
                        left-border-char
                        right-border-char
                        top-border-char
                        bottom-border-char
                        top-left-corner-char
                        top-right-corner-char
                        bottom-left-corner-char
                        bottom-right-corner-char]
                 :or {left-padding 0
                      right-padding 0
                      top-padding 0
                      bottom-padding 0
                      left-border-char "│"
                      right-border-char "│"
                      top-border-char "═"
                      bottom-border-char "═"
                      top-left-corner-char "╒"
                      top-right-corner-char "╕"
                      bottom-left-corner-char "╘"
                      bottom-right-corner-char "╛"}}]
  (box g
       :left-padding left-padding
       :right-padding right-padding
       :top-padding top-padding
       :bottom-padding bottom-padding
       :left-border-char left-border-char
       :right-border-char right-border-char
       :top-border-char top-border-char
       :bottom-border-char bottom-border-char
       :top-left-corner-char top-left-corner-char
       :top-right-corner-char top-right-corner-char
       :bottom-left-corner-char bottom-left-corner-char
       :bottom-right-corner-char bottom-right-corner-char))

(defn table
  "Similar table as clojure.pprint/print-table.

  Adapted from source-code of clojure.pprint/print-table."
  [ks rows & {:keys [nsew-char
                     nse-char
                     nsw-char
                     ewn-char
                     ews-char
                     ns-char
                     ew-char
                     nw-char
                     ne-char
                     se-char
                     sw-char
                     header?]
              :or {nsew-char \+
                   nse-char \+
                   nsw-char \+
                   ewn-char \+
                   ews-char \+
                   ns-char \|
                   ew-char \-
                   nw-char \+
                   ne-char \+
                   se-char \+
                   sw-char \+
                   header? true}}]
  (let [widths (map
                (fn [k]
                  (apply max
                         (count (str k))
                         (map #(count (str (get % k))) rows)))
                ks)
        spacers (map #(apply str (repeat % ew-char)) widths)
        fmts (map #(str "%" % "s") widths)
        fmt-row (fn [leader divider trailer row]
                  (str leader
                       (apply str
                              (interpose divider
                                         (for [[col fmt] (map vector (map #(get row %) ks) fmts)]
                                           (format fmt (str col)))))
                       trailer))
        top-border (text (fmt-row
                          (str se-char ew-char)
                          (str ew-char ews-char ew-char)
                          (str ew-char sw-char)
                          (zipmap ks spacers)))
        header-divider (text (fmt-row
                              (str nse-char ew-char)
                              (str ew-char nsew-char ew-char)
                              (str ew-char nsw-char)
                              (zipmap ks spacers)))
        bottom-border (text (fmt-row
                             (str ne-char ew-char)
                             (str ew-char ewn-char ew-char)
                             (str ew-char nw-char)
                             (zipmap ks spacers)))]
    (l/valign [top-border
               (when header?
                 (text (fmt-row
                        (str ns-char \space)
                        (str \space ns-char \space)
                        (str \space ns-char)
                        (zipmap ks ks))))
               (when header? header-divider)
               (l/valign (map (fn [row]
                                (text (fmt-row (str ns-char \space)
                                               (str \space ns-char \space)
                                               (str \space ns-char)
                                               row)))
                              rows))
               bottom-border])))

(defn table0
  ([ks rows]
   (table0 ks rows true))
  ([ks rows header?]
   (table ks rows
          :nsew-char " "
          :nse-char " "
          :nsw-char " "
          :ewn-char " "
          :ews-char " "
          :ns-char " "
          :ew-char " "
          :se-char " "
          :sw-char " "
          :ne-char " "
          :nw-char " "
          :header? header?)))

(defn table1
  ([ks rows]
   (table1 ks rows true))
  ([ks rows header?]
   (table ks rows
          :nsew-char "┼"
          :nse-char "├"
          :nsw-char "┤"
          :ewn-char "┴"
          :ews-char "┬"
          :ns-char "│"
          :ew-char "─"
          :se-char "┌"
          :sw-char "┐"
          :ne-char "└"
          :nw-char "┘"
          :header? header?)))

(defn table2
  ([ks rows]
   (table2 ks rows true))
  ([ks rows header?]
   (table ks rows
          :nsew-char "┼"
          :nse-char "├"
          :nsw-char "┤"
          :ewn-char "┴"
          :ews-char "┬"
          :ns-char "│"
          :ew-char "─"
          :se-char "╭"
          :sw-char "╮"
          :ne-char "╰"
          :nw-char "╯"
          :header? header?)))

(defn table3
  ([ks rows]
   (table3 ks rows true))
  ([ks rows header?]
   (table ks rows
          :nsew-char "."
          :nse-char "."
          :nsw-char ":"
          :ewn-char ":"
          :ews-char "."
          :ns-char ":"
          :ew-char "."
          :se-char "."
          :sw-char "."
          :ne-char ":"
          :nw-char ":"
          :header? header?)))

(defn matrix
  ([ks rows]
   (matrix ks rows true))
  ([ks rows header?]
   (l/halign [(vline (+ (if header? 4 2)
                        (count rows))
                     "│" "╭" "╰")
              (table0 ks rows header?)
              (vline (+ (if header? 4 2)
                        (count rows))
                     "│" "╮" "╯")])))

(defn tree
  ([node]
   (tree node 2 0))
  ([node text-wrapper-fn]
   (tree node 2 0 text-wrapper-fn))
  ([node x-padding y-padding]
   (tree node x-padding y-padding box1))
  ([node x-padding y-padding text-wrapper-fn]
   (cond
     (sequential? node) (l/valign
                         (for [n node]
                           (if (sequential? n)
                             (l/halign (map
                                        #(tree % x-padding y-padding text-wrapper-fn) n)
                                       x-padding 0)
                             (tree n x-padding y-padding text-wrapper-fn)))
                         0 y-padding)
     (c/grid? node) node
     :else (text-wrapper-fn (text (str node)) :left-padding 1 :right-padding 1))))

(defn chart-xy
  [xs ys & {:keys [point-symbol
                   draw-axis
                   x-label
                   y-label]
            :or {point-symbol \*
                 draw-axis true
                 x-label "x"
                 y-label "y"}}]
  (let [ks (map vector xs ys)
        p (as-> (c/empty-grid) $
            (apply assoc $ (interleave ks (repeat point-symbol))))

        p (if draw-axis
            (let [x-axis (l/===
                          1
                          (right-arrow (+ (:width p) 2) \-)
                          (text x-label))
                  y-axis (l/||
                          0
                          (vline (+ (:height p) 2) \| \| "▲")
                          (text y-label))
                  with-x-axis (assoc p [0 0] x-axis)
                  with-xy-axis (assoc with-x-axis [0 0] y-axis)]
              (c/add with-xy-axis p)) p)]
    (c/transform p (c/tf-vflip))))

(defn chart-bar
  [ns & {:keys [labels
                max-length
                bar-symbol
                horizontal]
         :or {max-length 40
              labels ns
              horizontal true}}]
  {:pre [(pos? max-length)]}
  (let [min-n (apply min ns)
        max-n (apply max ns)
        max-delta (- max-n min-n)
        unit (/ max-length (if (zero? max-delta) max-length max-delta))
        scaled-ns (map (fn [v] (c/round (* v unit))) ns)
        bar-symbol (cond
                     bar-symbol bar-symbol
                     horizontal "■"
                     :else "*")
        text-labels (if horizontal
                      (take (count ns) (map #(text (str %)) labels))
                      (take (count ns) (map (fn [l]
                                              (-> (str l)
                                                  text
                                                  (c/transform (c/tf-transpose))))
                                            labels)))]
    (if horizontal
      (l/valign (for [[n text-label] (map vector scaled-ns text-labels)]
                  (l/=== 1 (hline n bar-symbol) text-label)))
      (let [chart (l/halign (for [n scaled-ns]
                              (vline n bar-symbol)) 2 0)
            flipped-chart (c/transform chart (c/tf-vflip))]
        (assoc flipped-chart
               [0 0]
               (l/halign text-labels 2 0))))))
