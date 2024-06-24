(ns build
  (:require [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as dd]))

(def lib 'io.github.mn-dimension/plotly-pyclj)
(def version "0.1.10")
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn" :aliases [:clj]}))
(def jar-file (format "target/%s-%s.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn jar [_]
  (println "Building jar" jar-file)
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :src-dirs ["src"]
                :pom-data [[:licenses
                            [:license
                             [:name "Eclipse Public License 1.0"]
                             [:url "https://opensource.org/license/epl-1-0/"] ]]]})

  (b/copy-dir {:src-dirs ["src/clj"
                          "src/cljc"
                          "resources"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file}))

(defn install [_]
  (println "Installing jar" jar-file)
  (b/install {:basis basis
              :lib lib
              :version version
              :jar-file jar-file
              :class-dir class-dir})
  (println "Add to deps as:" lib "version" version))

(defn deploy [_]
  (dd/deploy {:installer :remote
              :artifact jar-file
              :pom-file (b/pom-path {:lib lib :class-dir class-dir})}))
