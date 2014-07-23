(defproject deadman "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :uberjar-name "deadman-standalone.jar"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.3.0"]
                 [compojure "1.1.8"]
                 [ring/ring-json "0.3.1"]
                 [ring-json-params "0.1.0"]
                 [org.clojure/data.json "0.2.5"]]
  :plugins [[lein-ring "0.8.11"]]
  :resource-paths ["lib/*"]
  :min-lein-version "2.0.0")
