(defproject org.clojars.rorokimdim/pp-grid "0.1.8"
  :description "A clojure library for constructing formatted text."
  :url "https://github.com/rorokimdim/pp-grid"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}

  :plugins [[cider/cider-nrepl "0.28.1"]
            [mx.cider/enrich-classpath "1.5.2"]
            [lein-ancient "1.0.0-RC3"]
            [lein-shell "0.5.0"]]

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [potemkin "0.4.5"]]


  :repl-options {:init-ns pp-grid.repl}
  :aliases {"lint" ["shell" "clj-kondo" "--lint" "src" "test" "dev"]}


  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :repl {:source-paths ["dev"]
                    :dependencies [[criterium "0.4.6"]]}})
