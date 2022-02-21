(ns pp-grid.api-test
  (:require [clojure.test :refer [deftest is testing]]

            [pp-grid.api :as g]
            [pp-grid.examples :as e]))

(deftest test-empty-grid
  (testing "grid constructors"
    (let [grid (g/empty-grid)]
      (is (= 2 (:dimension grid)))
      (is (= 0 (:width grid)))
      (is (= 0 (:height grid)))
      (is (nil? (:mins grid)))
      (is (nil? (:maxs grid))))
    (let [grid (g/empty-grid 1)]
      (is (= 1 (:dimension grid)))
      (is (= 0 (:width grid)))
      (is (= 0 (:height grid)))
      (is (nil? (:mins grid)))
      (is (nil? (:maxs grid))))))

(deftest test-width
  (testing "string-widths"
    (is (= 0 (g/width "")))
    (is (= 0 (g/width "\n123")))
    (is (= 1 (g/width "1")))
    (is (= 1 (g/width "1\n1234")))
    (is (= 4 (g/width "abcd\nefgh"))))
  (testing "string-convertible-widths"
    (is (= 1 (g/width 1)))
    (is (= 4 (g/width 1234)))
    (is (= 7 (g/width 1234.26)))
    (is (= 2 (g/width :a)))
    (is (= 5 (g/width :abcd))))
  (testing "grid-widths"
    (is (= 0 (g/width (g/empty-grid))))
    (is (= 5 (g/width (g/text "HELLO"))))))

(deftest test-height
  (testing "string-heights"
    (is (= 1 (g/height "")))
    (is (= 2 (g/height "\n123")))
    (is (= 1 (g/height "1")))
    (is (= 2 (g/height "1\n1234")))
    (is (= 2 (g/height "abcd\nefgh"))))
  (testing "string-convertible-heights"
    (is (= 1 (g/height 1)))
    (is (= 1 (g/height 1234)))
    (is (= 1 (g/height 1234.26)))
    (is (= 1 (g/height :a)))
    (is (= 1 (g/height :abcd))))
  (testing "grid-heights"
    (is (= 0 (g/height (g/empty-grid))))
    (is (= 1 (g/height (g/text "HELLO"))))
    (is (= 4 (g/height (g/text "HELLO\nWorld\nof\ntesting"))))))

(deftest test-grid-2d
  (testing "2d grid"
    (let [grid (-> (g/empty-grid 2)
                   (assoc [0 0] \H
                          [1 0] \E
                          [2 0] \L
                          [3 0] \L
                          [4 0] \O))]
      (is (= 5 (:width grid)))
      (is (= 1 (:height grid)))
      (is (= 0 (:min-x grid)))
      (is (= 4 (:max-x grid)))
      (is (= 0 (:min-y grid)))
      (is (= 0 (:max-y grid)))
      (is (= "HELLO" (g/render grid))))))

(deftest test-text
  (testing "text grids"
    (is (= "hello" (g/text "hello")))
    (is (= "  hello  " (g/text "hello" 2 2)))
    (is (= "***hello**" (g/text "hello" 3 2 \*)))
    (is (= "1" (g/text 1)))
    (is (= ":a" (g/text :a)))
    (is (= "(+ 1 2 3)" (g/text '(+ 1 2 3))))))

(deftest test-examples
  (testing "grid illustration"
    (let [grid (e/make-grid-illustration 4 4)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 40))
      (is (= (:height grid) 18))
      (is (g/grid? grid))))
  (testing "hello world"
    (let [grid (e/make-hello-world)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 11))
      (is (= (:height grid) 1))
      (is (g/grid? grid))))
  (testing "abcd"
    (let [grid (e/make-abcd)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 11))
      (is (= (:height grid) 11))
      (is (g/grid? grid))))
  (testing "boxed abcd"
    (let [grid (e/make-boxed-abcd)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 15))
      (is (= (:height grid) 13))
      (is (g/grid? grid))))
  (testing "haligned boxes"
    (let [grid (e/make-haligned-boxes)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 35))
      (is (= (:height grid) 7))
      (is (g/grid? grid))))
  (testing "tables"
    (let [grid (e/make-tables)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 50))
      (is (= (:height grid) 41))
      (is (g/grid? grid))))
  (testing "nested-table"
    (let [grid (e/make-nested-table)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 74))
      (is (= (:height grid) 17))
      (is (g/grid? grid))))
  (testing "colored-table"
    (let [grid (e/make-colored-table)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 37))
      (is (= (:height grid) 8))
      (is (g/grid? grid))))
  (testing "colored-boxes"
    (let [grid (e/make-colored-boxes)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 50))
      (is (= (:height grid) 13))
      (is (g/grid? grid))))
  (testing "decorated text"
    (let [grid (e/make-decorated-text "HELLO")]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 5))
      (is (= (:height grid) 1))
      (is (g/grid? grid))))
  (testing "tree"
    (let [grid (e/make-tree)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 22))
      (is (= (:height grid) 19))
      (is (g/grid? grid))))
  (testing "chart-xy"
    (let [grid (e/make-chart-xy)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 17))
      (is (= (:height grid) 7))
      (is (g/grid? grid))))
  (testing "chart-bar"
    (let [grid (e/make-chart-bar)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 46))
      (is (= (:height grid) 5))
      (is (g/grid? grid))))
  (testing "chart-bar-vertical"
    (let [grid (e/make-chart-bar-vertical)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 10))
      (is (= (:height grid) 16))
      (is (g/grid? grid))))
  (testing "transformations"
    (let [grid (e/make-transformations)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 23))
      (is (= (:height grid) 20))
      (is (g/grid? grid))))
  (testing "diagram"
    (let [grid (e/make-diagram)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 82))
      (is (= (:height grid) 12))
      (is (g/grid? grid)))))
