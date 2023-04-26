(ns user
  (:require [pp-grid.api :as g]
            [pp-grid.examples :as e]))

(defn p [x]
  (println (str x)))

(do
  (p (e/make-abcd))
  (p (e/make-boxed-abcd))
  (p (e/make-haligned-boxes))
  (p (e/make-tables))
  (p (e/make-nested-table))
  (p (e/make-decorated-text "Hello bb"))
  (p (e/make-colored-table))
  (p (e/make-colored-boxes))
  (p (e/make-tree))
  (p (e/make-chart-xy))
  (p (e/make-chart-bar))
  (p (e/make-transformations))
  (p (e/make-diagram)))
