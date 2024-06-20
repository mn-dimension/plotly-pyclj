(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'dimension/plotly-pyclj)
(def version "0.1.9")
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
                :src-dirs ["src"]})
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

