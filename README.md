# web-sink

Yet another micro-blog/pastebin/bulletin-board webserver powered by Clojure, enlive, hiccup, compojure, jetty.

##Current status
- on startup:
  - create new sqlite database + tables if it doesn't exists
  - create new web server(jetty)
- added routing rule for POST to save new message in the db
- added routing rule for GET to retrieve messages from the db
  - html response is generated using an enlive html template and hiccup snippets
- no users/authentication yet -another web server(ex. nginx) could be used to allow write access (POST urls) for authenticated users only

##Future plans
 - add support for users register/login/admin
 - message threads
 - user groups
 - BBCode support
 - different privacy levels user/group/all/public
 - modify posts (delete, edit content, visibility, etc.)
 - threaded and flat view
 - preview
 - styles for standalone posts etc.

## Usage

lein run

Use a web browser to connect to the web server on port 3001 (or re-route with another web server ex. nginx) and start adding/reading content/comments.

## License

Copyright © 2016

Distributed under the Eclipse Public License either version 1.0.
