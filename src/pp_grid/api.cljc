(ns pp-grid.api
  #?(:bb
     (:require [pp-grid.core]
               [pp-grid.ansi-escape-code]
               [pp-grid.layout]
               [pp-grid.object])
     :clj
     (:require [potemkin :refer [import-vars]]

               [pp-grid.core]
               [pp-grid.ansi-escape-code]
               [pp-grid.layout]
               [pp-grid.object])))

#?(:bb
   (do
     (defmacro import-public-vars [ns]
       `(do ~@(for [[n# v#] (map (partial take 2) (ns-publics ns))]
                (let [meta# (meta v#)
                      doc# (:doc meta#)]
                  (if (nil? doc#)
                    `(def ~n# ~(symbol (str ns "/" n#)))
                    `(def ~n# ~doc# ~(symbol (str ns "/" n#))))))))

     (import-public-vars pp-grid.core)
     (import-public-vars pp-grid.object)
     (import-public-vars pp-grid.layout)
     (import-public-vars pp-grid.ansi-escape-code))

   :clj
   (import-vars
    [pp-grid.core

     add
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
     box0
     box1
     box2
     box3
     box4
     box5
     chart-bar
     chart-xy
     decorate
     fill
     hfill
     hline
     matrix
     table
     table*
     table0
     table1
     table2
     table3
     text
     tree
     vfill
     vline]

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

     ESCAPE-CODE-RESET]))
