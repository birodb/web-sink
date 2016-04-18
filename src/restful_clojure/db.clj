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

(defonce db-table-prep!
  (create-table-if-not-exists!
   conn
   "people" "id" "name, email"))

(defn db-add-row!
    [name email]
    (j/execute! conn ["INSERT INTO people (name, email) VALUES (?, ?);" name email]))

;(j/execute! conn ["INSERT INTO people (name, email) VALUES ('hugo', 'hugo@test.com');"])
;(j/execute! conn ["INSERT INTO people (name, email) VALUES ('emil', 'emil@test.com');"])
;(j/execute! conn ["INSERT INTO people (name, email) VALUES ('admin', 'admin@test.com');"])
;(j/execute! conn ["INSERT INTO people (name, email) VALUES ('dozer', 'dozer@test.com');"])
;(load-table-data conn 'people)
;CREATE TABLE "users" ("user_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , "user_name" VARCHAR(64), "user_email" VARCHAR(100), "user_regdate" DATETIME DEFAULT CURRENT_TIMESTAMP, "user_type" INTEGER, "user_regip" VARCHAR(40), "user_groupid" INTEGER DEFAULT 0, "user_allowhtml" BOOL, "user_posts" INTEGER DEFAULT 0, "user_actkey" VARCHAR(40), "user_password" VARCHAR(40))
;INSERT INTO "users" VALUES (null,"bob6","bob1@bob.com", CURRENT_TIMESTAMP,"3",null,"0","1","0",null,null);
