(ns backend.database.setup
  (:require [datomic.client.api :as d]))

;; Remember to start a peer server with
;; the command
;; bin/run \
;;  -m datomic.peer-server \
;;  -h localhost \
;;  -p 8998 \
;;  -a clojure_fullstack_key,clojure_fullstack_secret \
;;  -d clojure_fullstack,datomic:mem://clojure_fullstack

;; Create a config object
(def config {:server-type        :peer-server
             :access-key         "clojure_fullstack_key"
             :secret             "clojure_fullstack_secret"
             :endpoint           "localhost:8998"
             :validate-hostnames false})

;; Create a client
(def client (d/client config))

;; Create a connection with the client
(def conn (d/connect client {:db-name "clojure_fullstack"}))
