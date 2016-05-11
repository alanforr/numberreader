(defproject numberreader "0.2.0-SNAPSHOT"
  :description "A Clojure library designed to read a number between 0 and 10^306-1 inclusive,
  and translate it into a gramatically correct representation."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-codox "0.9.4"]]
  :codox {:namespaces [numberreader.core]}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]])
