(ns my-bottle-message.core
 (:require [compojure.core :refer :all]
           [compojure.route :as route]
           [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
           [ring.adapter.jetty :as ring]
           [hiccup.form :as hf]
           [hiccup.page :as hp]
           [hiccup.core :refer :all]
           [ring.util.anti-forgery :as rf]
           [cheshire.core :refer :all]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn default
  [title & page-content]
  (hp/html5 {:lang "en"}
            [:head
              [:title title]
              (hp/include-css "/site.css")]
            [:body
              [:div.site [:div.header
                          [:h1 "✉️️ Message in a Bottle \uD83C\uDFDD️"]
                          [:h3 "The social network for deserted islanders"]]
                [:div.page-content page-content]]]))

(defn sent-message
  [message-response]
  (default "Sent Message"
    [:h1 "Your message was sent!"]
    [:p "Would you like to read another message?"]
    [:a {:href "/read-message"} "\uD83D\uDCE8 Read another message"]))


(defn send-message
  [validation-message]
  (default "Send Message"
    [:p "Write your message below. Unfortunately, the paper can only hold 250 characters."]
    (if (nil? validation-message) [:span] [:p validation-message])
    (hf/form-to [:post "/send-message"]
                [:textarea {:name "message" :autofocus "true" :rows "10" :id "test"}]
                [:input {:type "submit" :value "Send"}]
                (rf/anti-forgery-field))))

(defn test
  []
  (default
   "test"
   [:p "fuck"]))


(defn test2
  []
  (hp/html5
    [:h1 "fhcuk fuck"]))

(defn home
  []
  (default "Message ina Bottle"
    [:p "You found a bottle on the shore!"]
    [:a {:href "/read-message"} "\uD83D\uDCE8 Read message"]))


(defroutes app-routes
  (GET "/" [] (home))
  ;(GET "/read-message" [] (handlers/read-message))
  (GET "/send-message" [] (send-message nil))
  (POST "/send-message" [message] (sent-message message))
  (GET "/test" [] (test))
  (GET "/test2" [] (test2))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))


(defn -main []
  (ring/run-jetty app {:port (Integer. "3000") :join? false}))

(comment
  (-main))

;(-main)
