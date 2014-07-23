(ns deadman.core
  (:use compojure.core)
  (:require [clojure.data.json :as json]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [compojure.handler :as handler])
  (:import [com.twilio.sdk TwilioRestClient]))

(def timers (atom {}))

(def twilio-sid (System/getenv "TWILIO_ACCOUNT_SID"))
(def twilio-auth-token (System/getenv "TWILIO_AUTH_TOKEN"))
(def twilio-from-number (System/getenv "TWILIO_FROM_NUMBER"))

(defn twilio-configured? []
  (and twilio-sid
       twilio-auth-token
       twilio-from-number))

(defn send-sms [number message]
  (if (twilio-configured?)
    (let [client (new TwilioRestClient twilio-sid twilio-auth-token)
          message-factory (.getSmsFactory (.getAccount client))
          message-params {"Body" message "To" number "From" twilio-from-number}]
      (.create message-factory message-params)))
    (prn "Twilio not configured"))

(defn build-timer-thread [{timeout "timeout", number "number", message "message", :as timer-data}]
  (Thread.
    (fn []
      (Thread/sleep timeout)
      (send-sms number message))))

(defn maybe-kill-thread [thread]
  (if thread
    (.stop thread)))

; timers become {:data <what they PUT> :timer <the timer thing>}
(defn build-timer [new-timer old-timer]
  (maybe-kill-thread (:thread old-timer))
  (let [thread (build-timer-thread new-timer)]
    (.start thread)
    {:data new-timer :thread thread}))

(defn put-timer [key new-timer]
  (swap! timers
         (fn [map]
             (update-in map [key] (partial build-timer new-timer))))
  new-timer)

(defn handle-timer-request [{body :body rp :route-params :as request}]
  {:body (put-timer (:id rp) body)})

(defn get-timer [{rp :route-params}]
  (let [timer (get @timers (:id rp))]
    (if timer {:body (:data timer)}
              (route/not-found {:message "Not found"}))))

(defn reflect [request]
  (str request))

(defroutes main-routes
  (POST "/timers/:id" request (reflect request))
  (PUT "/timers/:id" request (handle-timer-request request))
  (GET "/timers/:id" request (get-timer request))
  (GET "/timers" request (fn [r] @timers))
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

; TODO: persist/load timers
; TODO: error handling for missing params
