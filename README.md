# pp-grid

[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.rorokimdim/pp-grid.svg)](https://clojars.org/org.clojars.rorokimdim/pp-grid)

pp-grid is a clojure library to easily construct formatted text.

It provides a grid data-structure (a `map`) and some primitive functions
to draw text on it.

Here is a `4 x 4` grid with `16` cells:

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

To try these examples in a repl, require pp-grid as follows.

```
(require '[pp-grid.api :as g])
```

All the examples are also available in `pp-grid.examples` namespace.

### ABCD

```
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

```
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

```
(defn make-haligned-boxes []
  (let [a (g/box (g/text "AB"))
        b (g/box1 (g/text "CD"))
        c (g/box2 (g/text "EF"))]
    (g/halign [a b c] 1 0)))

(make-haligned-boxes)
```

which gives

```
+--+ ┌──┐ ╒══╕
|AB| │CD│ │EF│
+--+ └──┘ ╘══╛
```

### Tables

```
(defn make-tables []
  (let [data [{:a 1 :b 2 :c 3}
              {:a 10 :b 20 :c 30}]
        t0 (g/table [:a :b] data)
        t1 (g/table1 [:a :b] data)
        t2 (g/table2 [:a :b] data)
        t3 (g/table3 [:a :b] data)
        matrix (g/matrix [:a :b] data)]
    (g/valign [t0 t1 t2 t3 matrix])))

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
╭           ╮
│  :a   :b  │
│           │
│   1    2  │
│  10   20  │
╰           ╯
```

### Decorated text

Any grid can be decorated using ansi-escape-codes:

```
(defn example-decorated-text [s]
  (-> s
      g/text
      (g/decorate g/ESCAPE-CODE-BACKGROUND-BLUE)))

(example-decorated-text "HELLO")
```

which prints "HELLO" in blue, if our terminal supports ansi-escape-codes. Depending
on how a repl is configured, we might need to `println` the output of the function to see the
blue color.

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
┌───┐
│ 2 │
└───┘
┌───┐  ┌───┐
│ 3 │  │ 4 │
└───┘  └───┘
┌────┐  ┌────┐  ┌────┐
│ :a │  │ :b │  │ 10 │
└────┘  └────┘  └────┘
                ┌────┐
                │ 20 │
                └────┘
```

### XY Chart

```clojure
(defn make-chart-xy []
  (g/chart-xy (range) [0 1 2 3 2 1 0 1 2 3 2 1 0]))

(make-chart-xy)
```

which gives

```
y
▲
|
|  *     *
| * *   * *
|*   * *   *
*-----*-----*-▶︎ x
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

## Credits

0. [Clojure](https://clojure.org/)
1. Inspiration from [boxes](https://hackage.haskell.org/package/boxes), [cl-spark](https://github.com/tkych/cl-spark)
2. [Potemkin](https://github.com/clj-commons/potemkin)
3. All of these [libraries](https://github.com/rorokimdim/pp-grid/blob/master/project.clj)


## License

Copyright © 2022 Amit Shrestha

This program and the accompanying materials are made available under [MIT License](https://opensource.org/licenses/MIT)
