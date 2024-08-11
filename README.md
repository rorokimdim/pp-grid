[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.rorokimdim/pp-grid.svg)](https://clojars.org/org.clojars.rorokimdim/pp-grid)
[![cljdoc badge](https://cljdoc.org/badge/org.clojars.rorokimdim/pp-grid)](https://cljdoc.org/d/org.clojars.rorokimdim/pp-grid)

# pp-grid

pp-grid is a clojure library to easily construct formatted text.

It provides a grid data-structure (a `map`) and some primitive functions
to draw text on it.

Here is a `5 x 5` grid with `25` cells:

```
|------------------------------------▶︎ x
|┌─────┐┌─────┐┌─────┐┌─────┐┌─────┐
|│(0,0)││(1,0)││(2,0)││(3,0)││(4,0)│
|└─────┘└─────┘└─────┘└─────┘└─────┘
|┌─────┐┌─────┐┌─────┐┌─────┐┌─────┐
|│(0,1)││(1,1)││(2,1)││(3,1)││(4,1)│
|└─────┘└─────┘└─────┘└─────┘└─────┘
|┌─────┐┌─────┐┌─────┐┌─────┐┌─────┐
|│(0,2)││(1,2)││(2,2)││(3,2)││(4,2)│
|└─────┘└─────┘└─────┘└─────┘└─────┘
|┌─────┐┌─────┐┌─────┐┌─────┐┌─────┐
|│(0,3)││(1,3)││(2,3)││(3,3)││(4,3)│
|└─────┘└─────┘└─────┘└─────┘└─────┘
|┌─────┐┌─────┐┌─────┐┌─────┐┌─────┐
|│(0,4)││(1,4)││(2,4)││(3,4)││(4,4)│
|└─────┘└─────┘└─────┘└─────┘└─────┘
▼
y
```

We can set any cell's value in a grid
with an `assoc`: `(assoc grid [x y] character)`.

To illustrate the idea, here is one (tedious) way to write "HELLO WORLD" to a grid:

```clojure
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

(make-hello-world)
```

which gives

```
HELLO WORLD
```

There are many functions available to make writing to a grid easier. For example, the following writes "HELLO WORLD" as well.

```clojure
(g/text "HELLO WORLD")
```

## Examples

All the examples assume we are using the latest version. Some examples
build up on earlier examples.

To try these examples in a repl, require pp-grid as follows.

```clojure
(require '[pp-grid.api :as g])
```

All the examples are also available in `pp-grid.examples` namespace.

Depending on our repl, the examples may print the map structure, instead of the string form.

We can convert any grid to its string form using `(str grid)` and print the string:

```clojure
(println (str grid))
```

### ABCD

```clojure
(defn make-abcd []
  (-> (g/empty-grid)
      (assoc [0 0] \A
             [10 0] \B
             [0 10] \C
             [10 10] \D
             [5 5] \*)))

(make-abcd)
```

which gives

```
A         B




     *




C         D
```

### Boxed ABCD

```clojure
(defn make-boxed-abcd []
  (-> (make-abcd)
      (g/box :left-padding 1 :right-padding 1)))

(make-boxed-abcd)
```

which gives

```
+-------------+
| A         B |
|             |
|             |
|             |
|             |
|      *      |
|             |
|             |
|             |
|             |
| C         D |
+-------------+
```

### Horizontally aligned boxes

```clojure
(defn make-haligned-boxes []
  (let [b (g/box "B")
        b0 (g/box0 "B0")
        b1 (g/box1 "B1")
        b2 (g/box2 "B2")
        b3 (g/box3 "B3")
        b4 (g/box4 "B4")
        b5 (g/box5 "B5" :left-padding 2 :right-padding 2 :top-padding 2 :bottom-padding 2)]
    (g/halign [b b0 b1 b2 b3 b4 b5] 1)))

(make-haligned-boxes)
```

which gives

```
+-+    ┌──┐ ╭──╮ .... ╒══╕ ********
|B| B0 │B1│ │B2│ :B3: │B4│ *      *
+-+    └──┘ ╰──╯ .... ╘══╛ *      *
                           *  B5  *
                           *      *
                           *      *
                           ********
```

### Tables

```clojure
(defn make-tables []
  (let [data [{:a 1 :b 2 :c 3}
              {:a 10 :b 20 :c 30}]
        t0 (g/table [:a :b] data)
        t1 (g/table1 [:a :b] data)
        t2 (g/table2 [:a :b] data)
        t3 (g/table3 [:a :b] data)
        matrix (g/matrix [:a :b] data)
        alphabets (g/table* (map #(g/box1 (char %)) (range 65 91)) 10)]
    (g/valign [t0 t1 t2 t3 matrix alphabets])))

(make-tables)
```

which gives

```
+----+----+
| :a | :b |
+----+----+
|  1 |  2 |
| 10 | 20 |
+----+----+
┌────┬────┐
│ :a │ :b │
├────┼────┤
│  1 │  2 │
│ 10 │ 20 │
└────┴────┘
╭────┬────╮
│ :a │ :b │
├────┼────┤
│  1 │  2 │
│ 10 │ 20 │
╰────┴────╯
...........
: :a : :b :
..........:
:  1 :  2 :
: 10 : 20 :
:....:....:
╭        ╮
│ :a  :b │
│        │
│  1   2 │
│ 10  20 │
╰        ╯

 ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐
 │A│  │B│  │C│  │D│  │E│  │F│  │G│  │H│  │I│  │J│
 └─┘  └─┘  └─┘  └─┘  └─┘  └─┘  └─┘  └─┘  └─┘  └─┘
 ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐
 │K│  │L│  │M│  │N│  │O│  │P│  │Q│  │R│  │S│  │T│
 └─┘  └─┘  └─┘  └─┘  └─┘  └─┘  └─┘  └─┘  └─┘  └─┘
 ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐  ┌─┐
 │U│  │V│  │W│  │X│  │Y│  │Z│
 └─┘  └─┘  └─┘  └─┘  └─┘  └─┘
```

We can also put another table (or a grid) inside a table.

```clojure
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

(make-nested-table)
```

which gives

```
..........................................................................
:  :a :  :b :                        :t0 :              :c :         :t1 :
.........................................................................:
: 100 : 200 : +----+----+----+----+----+ : +-------------+ : ┌────┬────┐ :
:     :     : | :a | :b | :c | :d | :e | : | A         B | : │ :a │ :b │ :
:     :     : +----+----+----+----+----+ : |             | : ├────┼────┤ :
:     :     : |  1 |  2 |  3 |  4 |  5 | : |             | : │  1 │  2 │ :
:     :     : | 10 | 20 | 30 | 40 | 50 | : |             | : │ 10 │ 20 │ :
:     :     : +----+----+----+----+----+ : |             | : └────┴────┘ :
:     :     :                            : |      *      | :             :
:     :     :                            : |             | :             :
:     :     :                            : |             | :             :
:     :     :                            : |             | :             :
:     :     :                            : |             | :             :
:     :     :                            : | C         D | :             :
:     :     :                            : +-------------+ :             :
:.....:.....:............................:.................:.............:
```

### Decorated text

Any grid can be decorated using ansi-escape-codes:

```clojure
(defn make-decorated-text [s]
  (-> s
      g/text
      (g/decorate g/ESCAPE-CODE-BACKGROUND-BLUE)))

(make-decorated-text "HELLO")
```

which prints "HELLO" in blue, if our terminal supports ansi-escape-codes. Depending
on how a repl is configured, we might need to `println` the output of the function to see the
blue color.

Tables can also be decorated by passing in a sequence of ansi-escape-codes. For example,

```clojure
(defn make-colored-table []
  (let [data [{:a 1 :b 2}
              {:a 10 :b 20}
              {:a 100 :b 200}
              {:a 1000 :b 2000}]]
    (g/table2 [:a :b] data true :right [g/ESCAPE-CODE-BACKGROUND-GREEN
                                        g/ESCAPE-CODE-BACKGROUND-BRIGHT-MAGENTA
                                        g/ESCAPE-CODE-BACKGROUND-BRIGHT-BLUE])))

(make-colored-table)
```

which will give us a table with header-row colored in green and the rest colored
in magenta and blue alternately. Again, depending on the repl configuration, we might
need to `println` the output to see the colors.

Boxes can be decorated by passing in `:fill-escape-codes` option.

```clojure
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

(make-colored-boxes)
```

### Trees

```clojure
(defn make-tree []
  (g/tree [1 2 [3 4] [:a :b [10 20]]]))

(make-tree)
```

which gives

```
┌───┐
│ 1 │
└───┘
│
┌───┐
│ 2 │
└───┘
│
┌───┐  ┌───┐
│ 3 │  │ 4 │
└───┘  └───┘
│
┌────┐  ┌────┐  ┌────┐
│ :a │  │ :b │  │ 10 │
└────┘  └────┘  └────┘
                │
                ┌────┐
                │ 20 │
                └────┘
```

### XY Chart

```clojure
(defn make-chart-xy []
  (g/chart-xy (range)
              [0 1 2 3 2 1 0 1 2 3 2 1 0]
              :max-height 3))

(make-chart-xy)
```

which gives

```
y
▲
|
|         *                   *
|      *     *             *     *
|  *             *     *             *
*-------------------*-------------------*-▶ x
```

### Bar Chart

```clojure
(defn make-chart-bar []
  (g/chart-bar [20 80 100 200 400]))

(make-chart-bar)
```

which gives

```
■■ 20
■■■■■■■■ 80
■■■■■■■■■■■ 100
■■■■■■■■■■■■■■■■■■■■■ 200
■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 400
```

For a vertical bar chart, we can set `:horizontal` to false:

```clojure
(defn make-chart-bar-vertical []
  (g/chart-bar [100 200 250 360] :horizontal false))

(make-chart-bar-vertical)
```

which gives

```
         █
         █
         █
         █
      █  █
      █  █
   █  █  █
   █  █  █
   █  █  █
   █  █  █
█  █  █  █
█  █  █  █
█  █  █  █
1  2  2  3
0  0  5  6
0  0  0  0
```

### Transformations

A grid can be transformed using the `transform` function.

It accepts a grid and a function that takes in a coordinate vector and
returns a coordinate vector.

```clojure
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

(make-transformations)
```

which gives

```
+----------+----------+
| A      B | B      A |
|          |          |
|          |          |
|          |          |
|    *     |     *    |
|          |          |
|          |          |
| C      D | D      C |
+----------+----------+
+----------+----------+
| C      D | D      C |
|          |          |
|          |          |
|    *     |     *    |
|          |          |
|          |          |
|          |          |
| A      B | B      A |
+----------+----------+
```

### Diagrams

May be. Easy ones can be composed with some effort.

Here is an example. Don't read too much into it :)

```clojure
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

(make-diagram)
```

which gives

```
+--------------------------------+
|          +----------+          |
|          | A      B |          |
|          |          |          |
|┌─┐       |          |          |                 +-------------------+         e
|│a│ ────▶ |          |       ┌─┐|       ┌─┐       |┌─┐       ■■ 10    |        /
|└─┘       |    *     | ────▶ │b│| ────▶ │c│ ────▶ |│d│ ────▶ ■■■■ 20  | ────▶ *
|          |          |       └─┘|       └─┘       |└─┘       ■■■■■■ 30|        \
|          |          |          |                 +-------------------+         f
|          | C      D |          |
|          +----------+          |
+--------------------------------+
```

## Babashka and GraalVM

Expected to be compatible with both Babashka and GraalVM. Please file an issue otherwise.

## Clerk notebooks

For viewing grids in a [clerk](https://github.com/nextjournal/clerk) notebook, we can
set a custom viewer.

For example, try

```clojure
(clerk/set-viewers! [{:pred g/grid?
                      :render-fn '(fn [s] (v/html [:pre s]))
                      :transform-fn str}])
```

If you learn to do something fancier please submit a pull-request to update
this section :)

## Credits

0. [Clojure](https://clojure.org/)
1. Inspiration from [boxes](https://hackage.haskell.org/package/boxes), [cl-spark](https://github.com/tkych/cl-spark)
2. [Potemkin](https://github.com/clj-commons/potemkin)
3. All of these [libraries](https://github.com/rorokimdim/pp-grid/blob/master/project.clj)


## License

Copyright © 2022 Amit Shrestha

This program and the accompanying materials are made available under [MIT License](https://opensource.org/licenses/MIT)
