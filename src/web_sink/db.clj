(ns web-sink.db
  (:require [clojure.java.jdbc :as j]))

(def my-connection
  {:subprotocol "sqlite"
   :subname "small-sample.sqlite"
   :classname "org.sqlite.JDBC"})

(defn load-table-data
  "This loads the data from a database table"
  [table-name]
  (j/query my-connection ["SELECT * FROM ?;" table-name]))

(defn create-table-if-not-exists!
  "create a table if not exists in db with pk as integer primary key"
  [table-name pk columns]
  (j/execute!
   my-connection
   [(str     
     ;(println (str 
     "CREATE TABLE IF NOT EXISTS "
     table-name 
     " ("
     pk
     " INTEGER PRIMARY KEY ASC, "
     columns
     ", Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP"
     ");")]))

(defonce prep-tables!
  (do
    (create-table-if-not-exists!
     "users" "id" "name, email, ip")
    (create-table-if-not-exists!
     "groups" "id" "name")
    (create-table-if-not-exists!
     "posts" "id" "usersid, content, permission")
    ))

(defn add-to-users!
  [name email]
  (j/execute!
   my-connection
   ["INSERT INTO users (name, email) VALUES (?, ?);" name email]))

(defn add-sample-data!
  []
  (do 
    (add-to-users! "admin" "admin@test.com")
    (add-to-users! "dozer" "dozer@test.com")
    (add-to-users! "hugo" "hugo@test.com")
    (add-to-users! "emil" "emil@test.com")))
 
;" VALUES (null,"bob6","bob1@bob.com", CURRENT_TIMESTAMP,"3",null,"0","1","0",null,null);
 ;CREATE TABLE "users" ("user_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , "user_name" VARCHAR(64), "user_email" VARCHAR(100), "user_regdate" DATETIME DEFAULT CURRENT_TIMESTAMP, "user_type" INTEGER, "user_regip" VARCHAR(40), "user_groupid" INTEGER DEFAULT 0, "user_allowhtml" BOOL, "user_posts" INTEGER DEFAULT 0, "user_actkey" VARCHAR(40), "user_password" VARCHAR(40))))
