(defproject web-sink "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-core "1.5.1"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-jetty-adapter "1.5.1"]
                 [ring/ring-defaults "0.2.2"]
                 [compojure "1.5.2"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.xerial/sqlite-jdbc "3.16.1"];need to use this on raspi/arm
                 [enlive "1.1.6"]
                 [cheshire "5.7.0"]
                 [hiccup "1.0.5"]
                 [org.clojure/tools.nrepl "0.2.12"]]
  :profiles {:dev {:dependencies [[ring/ring-devel "1.4.0"]
                                  [lein-ring "0.9.7"]]}}
;  :plugins [[lein-ring "0.9.7"]]
  :main web-sink.core
  :aot [web-sink.core]

  ;;based on leinigen/sample.project.clj
  :repl-options {;;specify the repl port explicitly to have a consistent access point
                 :port 4001
                 ;;on a raspi2 java isn't exactly fast
                 :timeout 120000
                 :init (create-web-server! false)
                 })
