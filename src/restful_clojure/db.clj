(ns restful-clojure.db
  (:require [clojure.java.jdbc :as j]))

(def conn {:subprotocol "sqlite"
           :subname "small-sample.sqlite"
           :classname "org.sqlite.JDBC"})

(defn load-table-data
  "This loads the data from a database table"
  [dbase table-name]
  (j/query dbase (str "SELECT * FROM " table-name ";")))

(defn create-table-if-not-exists!
  "create a table if not exists in db with pk as integer primary key"
  [dbase table-name pk columns]
  (j/execute!
   dbase
   [(str     
;   (println (str 
     "CREATE TABLE IF NOT EXISTS "
     table-name 
     " ("
     pk
     " INTEGER PRIMARY KEY ASC, "
     columns
     ", Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP"
     ");")]))

(defonce db_table_prep!
  (create-table-if-not-exists!
   conn
   "people" "id" "name, email"))

;(j/execute! conn ["INSERT INTO people (name, email) VALUES ('hugo', 'hugo@test.com');"])
;(j/execute! conn ["INSERT INTO people (name, email) VALUES ('emil', 'emil@test.com');"])
;(j/execute! conn ["INSERT INTO people (name, email) VALUES ('admin', 'admin@test.com');"])
;(j/execute! conn ["INSERT INTO people (name, email) VALUES ('dozer', 'dozer@test.com');"])
;(load-table-data conn 'people)
