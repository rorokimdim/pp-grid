(ns pp-grid.api
  (:require [potemkin :refer [import-vars]]

            [pp-grid.core]
            [pp-grid.ansi-escape-code]
            [pp-grid.layout]
            [pp-grid.object]))

(import-vars
 [pp-grid.core

  add
  decorate
  empty-grid
  grid?
  height
  render
  render-grid
  subtract
  update-ranges
  width

  tf-hflip
  tf-project
  tf-rotate
  tf-rotate-90-degrees
  tf-scale
  tf-shear
  tf-translate
  tf-transpose
  tf-vflip
  transform

  ++
  --]

 [pp-grid.layout

  valign
  halign
  vspacer
  hspacer
  pull
  spacer

  ===
  ||]

 [pp-grid.object

  arrow-down
  arrow-left
  arrow-left-right
  arrow-ne
  arrow-nw
  arrow-right
  arrow-se
  arrow-sw
  arrow-up
  arrow-up-down
  box
  box1
  box2
  fill
  hfill
  hline
  text
  tree
  vfill
  vline

  table
  table0
  table1
  table2
  table3
  matrix

  chart-bar
  chart-xy]

 [pp-grid.ansi-escape-code

  ESCAPE-CODE-BOLD
  ESCAPE-CODE-UNDERLINE
  ESCAPE-CODE-REVERSED

  ESCAPE-CODE-BLACK
  ESCAPE-CODE-BLUE
  ESCAPE-CODE-CYAN
  ESCAPE-CODE-GREEN
  ESCAPE-CODE-MAGENTA
  ESCAPE-CODE-RED
  ESCAPE-CODE-WHITE
  ESCAPE-CODE-YELLOW

  ESCAPE-CODE-BRIGHT-BLACK
  ESCAPE-CODE-BRIGHT-BLUE
  ESCAPE-CODE-BRIGHT-CYAN
  ESCAPE-CODE-BRIGHT-GREEN
  ESCAPE-CODE-BRIGHT-MAGENTA
  ESCAPE-CODE-BRIGHT-RED
  ESCAPE-CODE-BRIGHT-WHITE
  ESCAPE-CODE-BRIGHT-YELLOW

  ESCAPE-CODE-BACKGROUND-BLACK
  ESCAPE-CODE-BACKGROUND-BLUE
  ESCAPE-CODE-BACKGROUND-CYAN
  ESCAPE-CODE-BACKGROUND-GREEN
  ESCAPE-CODE-BACKGROUND-MAGENTA
  ESCAPE-CODE-BACKGROUND-RED
  ESCAPE-CODE-BACKGROUND-WHITE
  ESCAPE-CODE-BACKGROUND-YELLOW

  ESCAPE-CODE-BACKGROUND-BRIGHT-BLACK
  ESCAPE-CODE-BACKGROUND-BRIGHT-BLUE
  ESCAPE-CODE-BACKGROUND-BRIGHT-CYAN
  ESCAPE-CODE-BACKGROUND-BRIGHT-GREEN
  ESCAPE-CODE-BACKGROUND-BRIGHT-MAGENTA
  ESCAPE-CODE-BACKGROUND-BRIGHT-RED
  ESCAPE-CODE-BACKGROUND-BRIGHT-WHITE
  ESCAPE-CODE-BACKGROUND-BRIGHT-YELLOW

  ESCAPE-CODE-RESET])
