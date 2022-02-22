(ns pp-grid.core
  (:require [clojure.string :as s]
            [clojure.pprint]

            [potemkin :as p]

            [pp-grid.ansi-escape-code :as ecodes]))

(declare
 add
 compute-ranges
 escaped-char?
 grid?
 render
 width
 height
 tf-translate
 transform
 update-ranges
 valid-key?
 valid-value?
 validate-key
 validate-value)

(defrecord EscapedChar [escape-code value]
  Object
  (toString [this] (render this)))

#_:clj-kondo/ignore
(p/def-map-type Grid [m metadata]
  (get [this k default-value]
       (cond
         (= k :m) m
         (#{:mins :maxs :dimension} k) (get metadata k default-value)
         (= k :min-x) (first (:mins metadata))
         (= k :max-x) (first (:maxs metadata))
         (= k :min-y) (second (:mins metadata))
         (= k :max-y) (second (:maxs metadata))
         (= k :width) (width this)
         (= k :height) (height this)
         :else (if (valid-key? (:dimension metadata) k)
                 (get m k default-value)
                 default-value)))
  (assoc [this k v]
         (when (and (validate-key (:dimension metadata) k) (validate-value v))
           (if (grid? v)
             (let [ds (map - k (:mins v))]
               (add this (transform v (apply tf-translate ds))))
             (Grid. (assoc m k v) (update-ranges metadata k)))))
  (dissoc [this k]
          (when (validate-key (:dimension metadata) k)
            (let [new-g (Grid. (dissoc m k) metadata)
                  dimension (:dimension metadata)]
              (with-meta new-g (apply update-ranges {:dimension dimension} (keys new-g))))))
  (keys [this] (keys m))
  (meta [this] metadata)
  (empty [this] (Grid. {} {:dimension (:dimension metadata)}))
  (with-meta [this metadata] (Grid. m metadata))
  clojure.lang.IPersistentCollection
  (equiv
   [this x]
   (if (string? x)
     (= (render this) x)
     (and (or (instance? java.util.Map x) (map? x))
          (= x m))))
  Object
  (toString [this] (render this)))

(defn empty-grid
  ([]
   (empty-grid 2))
  ([dimension]
   {:pre [(integer? dimension)
          (pos? dimension)]}
   (Grid. {} {:dimension dimension})))

(defn width [x]
  (cond
    (string? x) (->> x
                     (take-while #(not= % \newline))
                     count)
    (grid? x) (if (empty? x)
                0
                (inc (- (:max-x x) (:min-x x))))
    :else (width (str x))))

(defn height [x]
  (cond
    (string? x) (->> x
                     (filter #(= % \newline))
                     count
                     inc)
    (grid? x) (if (empty? x) 0
                  (inc (- (:max-y x) (:min-y x))))
    :else (height (str x))))

(defn escaped-char? [x]
  (instance? EscapedChar x))

(defn grid? [x]
  (instance? Grid x))

(defn escaped-char [c escape-code]
  (->EscapedChar escape-code c))

(defn update-ranges
  [metadata & ks]
  (if (empty? ks)
    metadata
    (let [mins (:mins metadata nil)
          maxs (:maxs metadata nil)]
      (assoc metadata
             :mins (apply (partial map min)
                          (if (nil? mins) ks (conj ks mins)))
             :maxs (apply (partial map max)
                          (if (nil? maxs) ks (conj ks maxs)))))))

(defn add
  "Constructs a grid with all given grids added together."
  [& gs]
  (let [gs (remove empty? gs)]
    (cond
      (empty? gs) nil
      (= 1 (count gs)) (first gs)
      :else (let [ga (first gs)
                  metadata (meta ga)
                  new-m (persistent! (apply assoc!
                                            (transient (:m ga))
                                            (mapcat identity (apply concat (rest gs)))))]
              (Grid. new-m (apply update-ranges metadata (keys new-m)))))))

(defn subtract
  "Returns the first grid minus keys in rest of the grids."
  [& gs]
  (reduce
   (fn [ga gb]
     (cond
       (empty? gb) ga
       (empty? ga) (reduced ga)
       :else (apply dissoc ga (keys gb))))
   (first gs)
   (rest gs)))

(defn decorate
  "Decorates a grid with given ansi-escape-codes."
  [g & escape-codes]
  (reduce
   (fn [acc [k v]]
     (let [escape-code (apply str escape-codes)
           new-v (if (escaped-char? v)
                   (->EscapedChar (apply str escape-code (:escape-code v))
                                  (:value v))
                   (->EscapedChar escape-code v))]
       (assoc acc k new-v)))
   (empty g)
   g))

(defn ++
  "Convenience wrapper for add to accept grids as args."
  [& gs]
  (add gs))

(defn --
  "Convenience wrapper for subtract to accept grids as args."
  [& gs]
  (subtract gs))

(defn valid-key? [dimension k]
  (and
   (vector? k)
   (every? integer? k)
   (= (count k) dimension)))

(defn valid-value? [v]
  (contains? (methods render) (type v)))

(defn validate-key [dimension k]
  (when-not (valid-key? dimension k)
    (throw (IllegalArgumentException. (str "Key expected to be a vector of "
                                           dimension " integers"))))
  k)

(defn validate-value [v]
  (when-not (valid-value? v)
    (throw (IllegalArgumentException. "Value must be a char, a Grid or a EscapedChar")))
  true)

(defn round [n]
  (if (integer? n)
    n
    (Math/round (double n))))

(defn transform
  "Transforms a grid into another grid using given transformation function.

  A transformation function accepts a key (coordinate vector) and returns another key.

  For example transformation functions, take a look at the tf-* functions."
  ([g f] (transform g f (:dimension g)))
  ([g f dimension]
   (let [transformed (reduce
                      (fn [acc [k v]]
                        (let [new-k (into [] (map round (f k)))]
                          (assoc acc new-k v)))
                      (empty-grid dimension)
                      g)]
     (with-meta transformed (apply update-ranges
                                   {:dimension dimension}
                                   (keys transformed))))))

(defn tf-translate
  "Returns a function that translates a coordinate by given deltas.

  For example, ((tf-translate 10 20) [1 2]) = [11 22]."
  [& deltas]
  (fn [k]
    (map + k (take (count k) (concat deltas (repeat 0))))))

(defn tf-scale
  "Returns a function that scales a coordinate by given amounts.

  For example, ((tf-scale 10 2) [3 2]) = [30 4]."
  [& ns]
  (fn [k]
    (map * k (take (count k) (concat ns (repeat 1))))))

(defn tf-transpose
  "Returns a function that transposes a coordinate.

  Returns a function just to keep it consistent with other tf-*
  functions.

  For example, ((tf-transpose) [1 2]) = [2 1]."
  []
  (fn [k]
    (reverse k)))

(defn tf-hflip
  "Returns a function that horizontally flips a coordinate.

  Returns a function just to keep it consistent with other tf-*
  functions.

  For example, ((tf-hflip) [1 2]) = [-1 2]."
  []
  (fn [k]
    (let [[x y] (validate-key 2 k)]
      (list (- x) y))))

(defn tf-vflip
  "Returns a function that vertically flips a coordinate.

  Returns a function just to keep it consistent with other tf-*
  functions.

  For example, ((tf-vflip) [1 2]) = [1 -2]."
  []
  (fn [k]
    (let [[x y] (validate-key 2 k)]
      (list x (- y)))))

(defn tf-project
  "Returns a function that projects a coordinate into given dimension.

  For example, ((tf-project 2) [1 2 3 4 5]) = [1 2].

  A project-fn can be passed in to change how a coordinate is projected.
  By default, the identity function is used."
  ([target-dimension]
   (tf-project target-dimension identity))
  ([target-dimension project-fn]
   (fn [k]
     (take target-dimension (concat (project-fn k) (repeat 0))))))

(defn tf-rotate
  "Returns a function that rotates a coordinate by given angle in radians."
  [radians]
  (fn [k]
    (let [[x y] (validate-key 2 k)]
      (list
       (- (* x (Math/cos radians))
          (* y (Math/sin radians)))
       (+ (* x (Math/sin radians))
          (* y (Math/cos radians)))))))

(defn tf-rotate-90-degrees
  "Returns a function that rotates a coordinate by 90 degrees."
  []
  (fn [k]
    (let [[x y] (validate-key 2 k)]
      (list (- y) x))))

(defn tf-shear
  "Returns a function that shears a coordinate by given factors.

  See https://en.wikipedia.org/wiki/Shear_mapping.

  For example, ((tf-shear 2 3) [10 5]) = [10 + 2 * 5, 5 + 3 * 10] = [20 35]."
  [a b]
  (fn [[x y & zs]]
    (concat [(+ x (* a y)) (+ y (* b x))] zs)))

(defmulti ^String render-grid :dimension)

(defmethod render-grid 1 [g]
  (if (empty? g)
    nil
    (let [min-x (:min-x g)
          max-x (:max-x g)
          xs (range min-x (inc max-x))
          get-rendered (fn [x] (render (get g [x] " ")))]
      (apply str (map get-rendered xs)))))

(defmethod render-grid 2 [g]
  (if (empty? g)
    nil
    (let [min-x (:min-x g)
          min-y (:min-y g)
          max-x (:max-x g)
          max-y (:max-y g)
          xs (range min-x (inc max-x))
          ys (range min-y (inc max-y))
          get-rendered (fn [x y] (render (get g [x y] " ")))]
      (s/join
       \newline
       (for [y ys]
         (apply str (map get-rendered xs (repeat y))))))))

(defmethod render-grid :default [g]
  (if (empty? g)
    nil
    (render-grid (transform g (tf-project 2) 2))))

(defmulti ^String render type)

(defmethod render Grid [g]
  (if (empty? g)
    nil
    (render-grid g)))

(defmethod render EscapedChar [x]
  (str (:escape-code x)
       (:value x)
       ecodes/ESCAPE-CODE-RESET))

(defmethod render java.lang.Character [x]
  (str x))

(defmethod render String [x] x)

;;
;; Overwrite how Grid and EscapedChar get printed: Make them print the rendered string
;;

(defmethod print-method Grid [g ^java.io.Writer w]
  (.write w (render g)))

(defmethod print-method EscapedChar [x ^java.io.Writer w]
  (.write w (render x)))

(defmethod clojure.pprint/simple-dispatch Grid [g]
  (println (render g)))

(defmethod clojure.pprint/simple-dispatch EscapedChar [x]
  (println (render x)))
