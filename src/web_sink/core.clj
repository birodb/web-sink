(ns web-sink.core
  (:gen-class)
  ;(use 'ring.adapter.jetty)
  ; => nil
  (:require [hiccup.core :refer [html h]]
            ;[compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [GET POST ANY context defroutes]]
            [web-sink.db :as db]
            [net.cgrand.enlive-html :as ehtml]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [clojure.java.shell :refer [sh]]
            [cheshire.core :refer [generate-string]]))

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
    [:div {:style "border: 1px solid green; width: 800px; align: center;"}
     ;[:p (str "uri: " (:uri request))]
     ;[:p (str "query-string: " (h (:query-string request)))]
     ;[:p (str "remote-addr: " (h (:remote-addr request)))]
     [:p [:pre (h (generate-string (dissoc request :body) {:pretty true}))]]
     [:p (str "body: " (h (slurp (:body request))))]]
    [:br]
    [:div  {:style "border: 1px solid blue; width: 800px; align: center;"}
     [:a {:href ""} "Refresh"]
     [:form {:method 'post}
      [:fieldset
       [:legend "New post - bbcode is not yet supported"]
       ;[:input {:type 'text :name "newpost"}]
       [:input {:type 'hidden :name "redirecturl" :value (:url request)}]
       [:textarea {:rows 10 :cols 56 :name "newpost"}]
       [:br]
       [:input {:type 'submit :value "Send"}]]]]]])

(defn wrap-response
  "generate html response with header values set explicitly - or to be used in ring.adapter.jetty directly"
  [response]
  {:status 200
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body response})

(defn app-handler
  [request]
  (wrap-response (-> (html-from-request request) html)))


(ehtml/deftemplate index "web_sink/template1.html"
  [ctxt]
  [:p#message] (ehtml/content (:message ctxt)))

(comment (defn render [t]
    (apply str t)))

(comment (def render-to-response
    (comp response render))
)
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
          (record-post! request)
          (html-redirect (:redirecturl (:params request)))))
  (GET "/mas*" request (app-handler  request))
  (GET "/bar/*" [& args] (wrap-response (apply str (index {:message (apply str args)}))))
  (GET "/foo" request (wrap-response (apply str (index {:message "hard-coded"}))))
  (GET "/exec" request (wrap-response (html [:html [:body [:pre  (generate-string ((sh (:q (:params request))) :out))]]])))
  ;(( request "params") "q")
  (GET "/*" request (html (html-from-request request)))
  (route/resources "/")
  (route/not-found "<h1>Page not found<h1>"))

;(def app1
;(-> app-routes
;    handler/api
;    (comment wrap-json-params
;         wrap-json-response)))

;http://stackoverflow.com/questions/28904260/dynamic-handler-update-in-clojure-ring-compojure-repl
(def app
  (-> #'app-routes
      (wrap-defaults
       (assoc api-defaults
              :cookies true
              :proxy true
              :session {:flash true :store (cookie-store)}
              :security {:anti-forgery true}))))

;since the port can be acquired only once we create and start the server with defonce
(defonce wserver (run-jetty app {:port 3001 :join? false}))
;(defonce wserver (run-jetty app-handler {:port 3001 :join? false}))
;(defonce srv_start (new java.util.Date))

(defn now [] (new java.util.Date))

(defn mymain 
  [& args]
  ;(println app)
  (println "Dr. Bubo" " - " (now)))

(defn -main 
  [& args]
  (mymain args))
