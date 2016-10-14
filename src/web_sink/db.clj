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

(defn execute!
  [sql-expr]
  (j/execute! my-connection sql-expr))

(defn create-table-if-not-exists!
  "create a table if not exists in db with pk as integer primary key"
  [table-name pk columns]
  (execute!
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

(defn prep-tables!
  []
  (do
    (execute! ["CREATE TABLE IF NOT EXISTS users (user_id INTEGER PRIMARY KEY ASC, name TEXT NOT NULL, email TEXT NOT NULL, creation_ip TEXT, enabled BOOLEAN DEFAULT 0,  timestamp DATETIME DEFAULT CURRENT_TIMESTAMP);"])
    (execute! ["CREATE TABLE IF NOT EXISTS groups (group_id INTEGER PRIMARY KEY ASC, name TEXT NOT NULL, creator_id INTEGER, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(creator_id) REFERENCES users(user_id));"])
    (execute! ["CREATE TABLE IF NOT EXISTS usersgroups (user_id INTEGER, group_id INTEGER, creator_id INTEGER, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(user_id) REFERENCES users(user_id), FOREIGN KEY(group_id) REFERENCES groups(group_id), FOREIGN KEY(creator_id) REFERENCES users(user_id));"])
    (execute! ["CREATE TABLE IF NOT EXISTS posts (post_id INTEGER PRIMARY KEY ASC, text TEXT NOT NULL, user_id INTEGER, group_id INTEGER, creation_ip TEXT, permission INTEGER, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(user_id) REFERENCES users(user_id), FOREIGN KEY(group_id) REFERENCES groups(group_id));"])
    (add-sample-data!)))

(defonce create-tables prep-tables!)

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
