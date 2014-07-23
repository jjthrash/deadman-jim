(ns deadman.main
  (:require [deadman.core :as core]
            [ring.adapter.jetty :as jetty]))

(defn -main [port]
  (jetty/run-jetty core/app {:port (Integer. port) :join? false}))
