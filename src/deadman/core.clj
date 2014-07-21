(ns deadman.core
  (:use compojure.core)
  (:require [clojure.data.json :as json]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [compojure.handler :as handler]))

(def timers (atom {}))

(defn put-timer [key data]
  (swap! timers (fn [map] (assoc map key data)))
  data)

(defn handle-timer-request [{body :body rp :route-params :as request}]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (put-timer (:id rp) body)})

(defn get-timer [{rp :route-params}]
  (let [timer (get @timers (:id rp))]
    (if timer {:status 200
               :body timer}
              (route/not-found {:message "Not found"}))))

(defn reflect [request]
  (str request))

(defroutes main-routes
  (POST "/timers/:id" request (reflect request))
  (PUT "/timers/:id" request (handle-timer-request request))
  (GET "/timers/:id" request (get-timer request))
  (GET "/timers" request (fn [r] timers))
  (route/not-found {:message "Not found"}))

(def app
  (->
    (handler/api main-routes)
    (middleware/wrap-json-body)
    (middleware/wrap-json-response)))

(comment
(use 'ring.adapter.jetty)
(use 'deadman.core)
(run-jetty app {:port 3000})
)

; Need a service to accept a PUT
; PUT /timer/<uid>
; form vars: timeout, action
