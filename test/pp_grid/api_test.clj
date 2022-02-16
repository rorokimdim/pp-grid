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
      (is (= (:width grid) 41))
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
      (is (= (:width grid) 14))
      (is (= (:height grid) 3))
      (is (g/grid? grid))))
  (testing "tables"
    (let [grid (e/make-tables)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 13))
      (is (= (:height grid) 30))
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
      (is (= (:width grid) 18))
      (is (= (:height grid) 7))
      (is (g/grid? grid))))
  (testing "chart-bar"
    (let [grid (e/make-chart-bar)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 46))
      (is (= (:height grid) 5))
      (is (g/grid? grid))))
  (testing "transformations"
    (let [grid (e/make-transformations)]
      (is (= (:dimension grid) 2))
      (is (= (:width grid) 23))
      (is (= (:height grid) 20))
      (is (g/grid? grid)))))
