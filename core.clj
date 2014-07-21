(ns deadman.core)

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})

(comment
(use 'ring.adapter.jetty)
(use 'deadman.core)
(run-jetty handler {:port 3000})
)

