(ns pp-grid.object
  (:require [clojure.string :as s]

            [pp-grid.core :as c]
            [pp-grid.layout :as l]))

(defn text
  "Constructs a grid containing given string.

  If s is not a string, then (str s)
  will be used.

  Each line in the string is put in a new row."
  ([s]
   (text s 0 0))
  ([s pad-left pad-right]
   (text s pad-left pad-right \space))
  ([s pad-left pad-right pad-char]
   (if (string? s)
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
           (c/empty-grid)))
     (text (str s) pad-left pad-right pad-char))))

(defn hline
  "Constructs a horizontal line of given length.

  For example, (hline 3) is '---'."
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
  "Constructs a vertical line of given length.

  For example, (vline 3) is
  |
  |
  |
  "
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
  "Constructs a horizontal-filler of given size.

  For example, (hfill 3 \\*) is '***'."
  ([n]
   (hfill n \space))
  ([n c]
   (hline n c)))

(defn vfill
  "Constructs a vertical-filler of given size.

  For example, (vfill 3 \\*) is
  *
  *
  *
  "
  ([n]
   (vfill n \space))
  ([n c]
   (vline n c)))

(defn fill
  "Constructs a filler of given width and height.

  For example, (fill 4 4 \\*) is
  ****
  ****
  ****
  ****
  "
  ([w h]
   (fill w h \space))
  ([w h c]
   (l/valign (repeat h (hfill w c)))))

(defn left-arrow
  "Constructs a left-arrow of given length.

  For example, (left-arrow 4) is '◀───'."
  ([n]
   (left-arrow n \─ "◀"))
  ([n body-char]
   (left-arrow n body-char "◀"))
  ([n body-char head-char]
   (hline n body-char head-char body-char)))

(defn right-arrow
  "Constructs a right-arrow of given length.

  For example, (right-arrow 4) is '───▶︎'."
  ([n]
   (right-arrow n "─" "▶"))
  ([n body-char]
   (right-arrow n body-char "▶"))
  ([n body-char head-char]
   (hline n body-char body-char head-char)))

(defn left-right-arrow
  "Constructs a left-right-arrow of given length.

  For example, (left-right-arrow 4) is '◀──▶︎'."
  ([n]
   (left-right-arrow n \─ "◀" "▶︎"))
  ([n body-char]
   (left-right-arrow n body-char "◀" "▶︎"))
  ([n body-char left-head-char right-head-char]
   (hline n body-char left-head-char right-head-char)))

(defn up-arrow
  "Constructs a up-arrow of given length.

  For example, (up-arrow 4) is
  ▲
  │
  │
  │
  "
  ([n]
   (up-arrow n \│ "▲"))
  ([n body-char]
   (up-arrow n body-char "▲"))
  ([n body-char head-char]
   (vline n body-char head-char body-char)))

(defn down-arrow
  "Constructs a down-arrow of given length.

  For example, (down-arrow 4) is
  │
  │
  │
  ▼
  "
  ([n]
   (down-arrow n \│ "▼"))
  ([n body-char]
   (down-arrow n body-char "▼"))
  ([n body-char head-char]
   (vline n body-char body-char head-char)))

(defn up-down-arrow
  "Constructs a up-down-arrow of given length.

  For example, (up-down-arrow 4) is
  ▲
  │
  │
  ▼
  "
  ([n]
   (up-down-arrow n \│ "▲" "▼"))
  ([n body-char]
   (up-down-arrow n body-char "▲" "▼"))
  ([n body-char up-head-char down-head-char]
   (vline n body-char up-head-char down-head-char)))

(defn box
  "Constructs a grid wrapping given grid into a box.

  For example, (box (text \"HELLO\")) is
  +-----+
  |HELLO|
  +-----+
  "
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

(defn box1
  "Constructs a grid wrapping given grid into a box.

  Similar to box, but uses different border.

  For example, (box1 (text \"HELLO\")) is
  ┌─────┐
  │HELLO│
  └─────┘
  "
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

(defn box2
  "Constructs a grid wrapping given grid into a box.

  Similar to box, but uses different border.

  For example, (box2 (text \"HELLO\")) is
  ╒═════╕
  │HELLO│
  ╘═════╛
  "
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
  "Constructs a table.

  Produces similar table as clojure.pprint/print-table.

  Adapted from source-code of clojure.pprint/print-table.

  For example, (table [:a :b] [{:a 1 :b 2} {:a 3 :b 4}]) is
  +----+----+
  | :a | :b |
  +----+----+
  |  1 |  2 |
  |  3 |  4 |
  +----+----+
  "
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
                         (map #(c/width (get % k)) rows)))
                ks)
        spacers (map #(apply str (repeat % ew-char)) widths)
        fmts (map #(str "%" % "s") widths)
        fmt-row (fn [leader divider trailer row]
                  (let [vs (map #(get row %) ks)
                        heights (map c/height vs)
                        max-height (apply max heights)
                        ->cols (fn [i]
                                 (for [v vs]
                                   (nth (s/split-lines (str v)) i "")))]
                    (s/join
                     \newline
                     (for [i (range max-height)
                           :let [cols (->cols i)]]
                       (str leader (apply
                                    str
                                    (interpose divider
                                               (for [[col fmt] (map vector cols fmts)]
                                                 (format fmt (str col)))))
                            trailer)))))
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
  "Constructs a border-less table.

  For example, (table0 [:a :b] [{:a 1 :b 2} {:a 3 :b 4}]) is

    :a   :b

     1    2
     3    4
  "
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
  "Constructs a table.

  Similar to table but uses different border.

  For example, (table1 [:a :b] [{:a 1 :b 2} {:a 3 :b 4}]) is
  ┌────┬────┐
  │ :a │ :b │
  ├────┼────┤
  │  1 │  2 │
  │  3 │  4 │
  └────┴────┘
  "
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
  "Constructs a table.

  Similar to table but uses different border (rounded corners).

  For example, (table2 [:a :b] [{:a 1 :b 2} {:a 3 :b 4}]) is
  ╭────┬────╮
  │ :a │ :b │
  ├────┼────┤
  │  1 │  2 │
  │  3 │  4 │
  ╰────┴────╯
  "
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
  "Constructs a table.

  Similar to table but uses dotted border.

  For example, (table3 [:a :b] [{:a 1 :b 2} {:a 3 :b 4}]) is
  ...........
  : :a : :b :
  ..........:
  :  1 :  2 :
  :  3 :  4 :
  :....:....:
  "
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
  "Constructs a matrix.

  Similar to table, but changes the border to make it look more like a matrix.

  For example, (matrix [:a :b] [{:a 1 :b 2} {:a 3 :b 4}]) is
  ╭           ╮
  │  :a   :b  │
  │           │
  │   1    2  │
  │   3    4  │
  ╰           ╯
  "
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
  "Constructs a tree representation of a sequence.

  An element of the sequence can be another sequence or a grid or
  anything that can be converted to a string.

  For example, (tree [1 2 [3 [4 5] [6 7] 8]]) is
  ┌───┐
  │ 1 │
  └───┘
  │
  ┌───┐
  │ 2 │
  └───┘
  │
  ┌───┐  ┌───┐  ┌───┐  ┌───┐
  │ 3 │  │ 4 │  │ 6 │  │ 8 │
  └───┘  └───┘  └───┘  └───┘
         │      │
         ┌───┐  ┌───┐
         │ 5 │  │ 7 │
         └───┘  └───┘
  "
  ([node]
   (tree node 2 0))
  ([node text-wrapper-fn]
   (tree node 2 0 text-wrapper-fn))
  ([node x-padding y-padding]
   (tree node x-padding y-padding box1))
  ([node x-padding y-padding text-wrapper-fn]
   (tree node x-padding y-padding text-wrapper-fn "│"))
  ([node x-padding y-padding text-wrapper-fn branch-marker]
   (cond
     (sequential? node) (let [args (list x-padding
                                         y-padding
                                         text-wrapper-fn
                                         branch-marker)
                              children (for [n node]
                                         (if (sequential? n)
                                           (l/halign (map #(apply tree % args) n) x-padding 0)
                                           (apply tree n args)))]
                          (as-> children $
                            (interpose (text (str branch-marker)) $)
                            (l/valign $ 0 y-padding)))
     (c/grid? node) node
     :else (text-wrapper-fn (text (str node)) :left-padding 1 :right-padding 1))))

(defn chart-xy
  "Constructs a xy-chart (scatter plot).

  For example, (chart-xy (range) [0 1 2 1 0 1 2 1 0]) is
  y
  ▲
  |
  | *   *
  |* * * *
  *---*---*-▶︎ x
  "
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
  "Constructs a bar chart.

  For example, (chart-bar [10 20 30 40]) is
  ■■■■■■■■■■■■■ 10
  ■■■■■■■■■■■■■■■■■■■■■■■■■■■ 20
  ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 30
  ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 40
  "
  [ns & {:keys [labels
                max-length
                bar-symbol
                horizontal]
         :or {labels ns
              horizontal true}}]
  {:pre [(or (nil? max-length)
             (pos? max-length))]}
  (let [min-n (apply min ns)
        max-n (apply max ns)
        max-length (or max-length (if horizontal 40 10))
        max-delta (- max-n min-n)
        unit (/ max-length (if (zero? max-delta) max-length max-delta))
        scaled-ns (map (fn [v] (c/round (* v unit))) ns)
        bar-symbol (cond
                     bar-symbol bar-symbol
                     horizontal "■"
                     :else "█")
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
                              (or (vline n bar-symbol) (text ""))) 2 0)
            flipped-chart (c/transform chart (c/tf-vflip))]
        (assoc flipped-chart
               [0 0]
               (l/halign text-labels 2 0))))))
