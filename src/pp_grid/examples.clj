(ns pp-grid.examples
  (:require [pp-grid.api :as g]))

(defn make-grid-illustration [max-x max-y]
  (let [cells (->> (for [y (range (inc max-y))]
                     (for [x (range (inc max-x))]
                       (g/box1 (g/text (str "(" x "," y ")")))))
                   (map g/halign)
                   g/valign)
        x-axis (g/===
                1
                (g/arrow-right (+ (:width cells) 2) \-)
                (g/text "x"))
        y-axis (g/||
                0
                (g/arrow-down (+ (:height cells) 2) \|)
                (g/text "y"))]
    (g/halign [y-axis (g/valign [x-axis cells])] 0)))

(defn make-hello-world []
  (-> (g/empty-grid)
      (assoc [0 0] \H
             [1 0] \E
             [2 0] \L
             [3 0] \L
             [4 0] \O
             [5 0] \space
             [6 0] \W
             [7 0] \O
             [8 0] \R
             [9 0] \L
             [10 0] \D)))

(defn make-abcd []
  (-> (g/empty-grid)
      (assoc [0 0] \A
             [10 0] \B
             [0 10] \C
             [10 10] \D
             [5 5] \*)))

(defn make-boxed-abcd []
  (-> (make-abcd)
      (g/box :left-padding 1 :right-padding 1)))

(defn make-haligned-boxes []
  (let [a (g/box "AB")
        b (g/box1 "CD")
        c (g/box2 "EF" :left-padding 2 :right-padding 2)]
    (g/halign [a b c] 1)))

(defn make-tables []
  (let [data [{:a 1 :b 2 :c 3}
              {:a 10 :b 20 :c 30}]
        t0 (g/table [:a :b] data)
        t1 (g/table1 [:a :b] data)
        t2 (g/table2 [:a :b] data)
        t3 (g/table3 [:a :b] data)
        matrix (g/matrix [:a :b] data)]
    (g/valign [t0 t1 t2 t3 matrix])))

(defn make-nested-table []
  (let [data [{:a 1 :b 2 :c 3 :d 4 :e 5}
              {:a 10 :b 20 :c 30 :d 40 :e 50}]
        t0 (g/table [:a :b :c :d :e] data)
        t1 (g/table1 [:a :b] data)
        abcd (make-boxed-abcd)]
    (g/table3 [:a :b :t0 :c :t1]
              [{:a 100
                :b 200
                :t0 t0
                :c abcd
                :t1 t1}])))

(defn make-colored-table []
  (let [data [{:a 1 :b 2}
              {:a 10 :b 20}
              {:a 100 :b 200}
              {:a 1000 :b 2000}]]
    (g/table2 [:a :b] data true [g/ESCAPE-CODE-BACKGROUND-GREEN
                                 g/ESCAPE-CODE-BACKGROUND-BRIGHT-MAGENTA
                                 g/ESCAPE-CODE-BACKGROUND-BRIGHT-BLUE])))

(defn make-colored-boxes []
  (let [abcd (make-abcd)
        b0 (g/box1
            abcd
            :fill-escape-codes [g/ESCAPE-CODE-BACKGROUND-BLUE])
        b1 (g/box1
            abcd
            :fill-escape-codes [g/ESCAPE-CODE-BOLD
                                g/ESCAPE-CODE-RED])
        b2 (g/box0
            abcd
            :fill-escape-codes [g/ESCAPE-CODE-BOLD
                                g/ESCAPE-CODE-BRIGHT-GREEN
                                g/ESCAPE-CODE-BACKGROUND-BRIGHT-WHITE])
        b3 (g/box2
            abcd
            :fill-escape-codes [g/ESCAPE-CODE-BOLD
                                g/ESCAPE-CODE-RED
                                g/ESCAPE-CODE-UNDERLINE])]
    (g/halign [b0 b1 b2 b3])))

(defn make-decorated-text [s]
  (-> s
      g/text
      (g/decorate g/ESCAPE-CODE-BACKGROUND-BLUE)))

(defn make-tree []
  (g/tree [1 2 [3 4] [:a :b [10 20]]]))

(defn make-chart-xy []
  (g/chart-xy (range) [0 1 2 3 2 1 0 1 2 3 2 1 0]))

(defn make-chart-bar []
  (g/chart-bar [20 80 100 200 400]))

(defn make-chart-bar-vertical []
  (g/chart-bar [100 200 250 360] :horizontal false))

(defn make-transformations []
  (let [abcd (make-boxed-abcd)
        width (:width abcd)
        height (:height abcd)
        hflipped-abcd (g/transform abcd (g/tf-hflip))
        vflipped-abcd (g/transform abcd (g/tf-vflip))
        vflipped-hflipped-abcd (g/transform hflipped-abcd (g/tf-vflip))]
    (-> abcd
        (assoc [width 0] hflipped-abcd)
        (assoc [0 height] vflipped-abcd)
        (assoc [width height] vflipped-hflipped-abcd)
        (g/transform (g/tf-scale 0.75 0.75)))))

(defn make-diagram []
  (let [a (g/box1 "a")
        b (g/box1 "b")
        c (g/box1 "c")
        d (g/box1 "d")

        abcd (g/transform (make-boxed-abcd) (g/tf-scale 0.75 0.75))
        chart (g/chart-bar [10 20 30] :max-length 4)

        ra (g/arrow-right 5)
        c0 (-> (interpose ra [a abcd b])
               (g/halign 1 true)
               g/box)
        c1 (-> (interpose ra [d chart])
               (g/halign 1 true)
               g/box)
        c2 (-> (g/arrow-ne 3 "/" "*" "e")
               (assoc [0 0] (g/arrow-se 3 "\\" "*" "f")))]
    (g/halign (interpose ra [c0 c c1 c2]) 1 true)))
