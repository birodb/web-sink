(ns restful-clojure.core
  (:gen-class)
  ;(use 'ring.adapter.jetty)
  ; => nil
  (:require [hiccup.core :refer [html h]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [GET POST ANY context defroutes]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [restful-clojure.db :as db]
            [net.cgrand.enlive-html :as ehtml]
            [ring.adapter.jetty :refer [run-jetty]]))

(defn html-from-request [request]
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
       [:textarea {:rows 10 :cols 56 :name "newpost"}]
       [:br]
       [:input {:type 'submit :value "Send"}]]]]]])

(defn app-handler [request]
    {:status 200
        :headers {"Content-Type" "text/html;encoding=us-ascii"}
        :body (-> (html-from-request request) html)})

(ehtml/deftemplate index "restful_clojure/template1.html"
  [ctxt]
  [:p#message] (ehtml/content (:message ctxt)))

(defn render [t]
  (apply str t))

(def render-to-response
  ;  (comp response render)
  (comp render)
  )

(defroutes app-routes
  ;(GET "/" [] (resource-response "index.html" {:root "public"}))
  (ANY "/*" request (html (html-from-request request)))
  (route/resources "/")
  (route/not-found "<h1>Page not found<h1>"))

(def app
  (-> app-routes
      handler/api      
      ;wrap-json-params
      ;wrap-json-response
      ))

;(defonce wserver (run-jetty app-handler {:port 3001 :join? false}))
(defonce wserver (run-jetty app {:port 3001 :join? false}))

(defn now [] (new java.util.Date))

(defn mymain 
  [& args]
  ;(println app)
  (println "Dr. Bubo" " - " (now)))

(defn -main 
  [& args]
  (mymain args))

