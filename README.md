## web-sink

Yet another micro-blog/pastebin/bulletin-board webserver powered by Clojure.

###Current status
- on startup:
  - create new sqlite database + tables if it doesn't exists
  - create new web server(jetty)
- added routing rule for POST to save new message in the db
- added routing rule for GET to retrieve messages from the db
  - html is generated using hiccup - could be changed to enlive
- no users&login yet - could be used behind another web server for basic authentication/write access

###Future plans
-users/group/admin interface
-login/register
-BBCode support
-private/public content


### Usage

lein run

This will create a new sqlite database if it didn't exist and start the web server.  
Connect to it with a browser and start adding/reading comments.

### License

Copyright © 2016

Distributed under the Eclipse Public License either version 1.0.
