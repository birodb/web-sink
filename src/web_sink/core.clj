(ns web-sink.core
  (:gen-class)
  ;(use 'ring.adapter.jetty)
  ; => nil
  (:require [hiccup.core :refer [html h]]
            ;[compojure.handler :as handler]
            [ring.middleware.defaults :refer :all]
            [compojure.route :as route]
            [compojure.core :refer [GET POST ANY context defroutes]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [web-sink.db :as db]
            [net.cgrand.enlive-html :as ehtml]
            [ring.adapter.jetty :refer [run-jetty]]
            [clojure.java.shell :refer [sh]]))

(defn html-from-request
  "generate html hiccup response sample"
  [request]
  [:html 
   [:head 
    [:title "pastebin"]]
   [:body
    [:h3 {:class "header"} "Pastebin powered by clojure"]
    [:br]
    ;[:div (str "user.dir: " (System/getProperty "user.dir"))]
    [:div {:style "border: 1px solid green; width: 500px; align: center;"}
     ;[:p (str "uri: " (:uri request))]
     ;[:p (str "query-string: " (h (:query-string request)))]
     ;[:p (str "remote-addr: " (h (:remote-addr request)))]
     [:p (h (str request))]
     [:p (str "body: " (h (slurp (:body request))))]]
    [:br]
    [:div  {:style "border: 1px solid blue; width: 500px; align: center;"}
     [:a {:href ""} "Refresh"]
     [:form {:method 'post}
      [:fieldset
       [:legend "New post - bbcode is not yet supported"]
       ;[:input {:type 'text :name "newpost"}]
       [:input {:type 'hidden :name "redirecturl" :value (:url request)}]
       [:textarea {:rows 10 :cols 56 :name "newpost"}]
       [:br]
       [:input {:type 'submit :value "Send"}]]]]]])

(defn app-handler
  "generate html response with header values set explicitly - or to be used in ring.adapter.jetty directly"
  [request]
  {:status 200
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body (-> (html-from-request request) html)})

(ehtml/deftemplate index "web_sink/template1.html"
  [ctxt]
  [:p#message] (ehtml/content (:message ctxt)))

(defn render [t]
  (apply str t))

(comment (def render-to-response
    (comp response render)))

(defn record-post!
  [request]
  (db/add-to-users! (:newpost (:params request)) (str request)))

(defn html-redirect
  "redirect to new URL ex. after a POST action"
  [target-url]
  {:status 303
   :headers {"Location" target-url
             "Content-Type" "text/html; charset=UTF-8"}
   :body (html
          [:html
           [:body
            [:div "Please activate the link if not automatically redirected:"
             :br
             [:a {:href target-url} target-url]]]])})


(defroutes app-routes
                                        ;(GET "/" [] (resource-response "index.html" {:root "public"}))
                                        ;(ANY "/*" request (html (html-from-request request)))
  (POST "/*" request 
        (do
                                        ;(println request) 
          (record-post! request)
          (html-redirect (:redirecturl (:params request)))))
  (GET "/*" request (html (html-from-request request)))
  (route/resources "/")
  (route/not-found "<h1>Page not found<h1>"))

(def app1
  (-> app-routes
      handler/api
      (comment wrap-json-params
                 wrap-json-response)))

(def app
  (-> app-routes (wrap-defaults  site-defaults)))

;(defonce wserver (run-jetty app-handler {:port 3001 :join? false}))
;since the port can be acquired only once we create and start the server with defonce
(defonce wserver (run-jetty app {:port 3001 :join? false}))
;(defonce srv_start (new java.util.Date))

(defn now [] (new java.util.Date))

(defn mymain 
  [& args]
  ;(println app)
  (println "Dr. Bubo" " - " (now)))

(defn -main 
  [& args]
  (mymain args))
