# Set Up Datomic for local development

Datomic is a distributed database that is built upon the concept of
*datoms*. A **datom** is a *tuple* that has the following properties

1. *Entity id*: Allows grouping datoms that refer to the same entity.
2. *Name*: Is the name with which a property is indexed (e.g. `:person/name`)
3. *Value*: Is the value of a given entity.
4. *Transaction time*: It is a time reference of when the datom was inserted
5. *Status*: It can be `added` or `retracted`

Datomic persists the history of all data that has been added or modified
in its central DB. No register can be deleted. However, information can be
deprecated by *retracting it*.

Datoms are inserted/updated/retracted in *transactions*. Although a datom can be
added/updated/retracted individually, usually, they are more useful when grouped
using an entity id. Datoms can be unified by entity id, thus yielding properties
of this entity, and joins between entities can be performed, using a query
language called Datalog.

## Starting the peer server

To start using Datomic in a local environment, first need to get
[Datomic Pro](https://docs.datomic.com/pro/getting-started/get-datomic.html),
from the official website. Then, after unzipping the file, enter to
the distribution folder and run the command

```bash
bin/run \
  -m datomic.peer-server \
  -h localhost \
  -p 8998 \
  -a clojure_fullstack_key,clojure_fullstack_secret \
  -d clojure_fullstack,datomic:mem://clojure_fullstack
```

What this command does is to set up a *peer server*, which allows
the Datomic client to interact with the database, and trigger
transactions. In the command above:

| option |                 Description                  |
|:------:|:--------------------------------------------:|
|   -m   |     Create a peer server for the client      |
|   -h   |               server hostname                |
|   -p   |                     port                     |
|   -a   | access key, access secret for authentication |
|   -d   | Database name (use *mem*) for local storage  |

After running the command, you should get a message

```bash
Serving clojure_fullstack
```

## Setting up the deps.edn

I'm documenting the process of using the client library because
it is what you would use in a cloud environment. I'm going
to use a deps.edn project. So, configure your file to look like

```clojure
{:paths ["src/main"]
 :deps {org.clojure/clojure    {:mvn/version "1.11.1"}
        com.datomic/client-pro {:mvn/version "1.0.76"}}}
```

With this, you can require the datomic client library utilities in
your namespace as follows

```clojure
(ns backend.database.setup
  (:require [datomic.client.api :as d]))
```

## Enabling the connection to the server

To effectively perform queries and transactions, a client must be
created, and connected to the local server. To do this, first define
a configuration map for the connection

```clojure
(def config {:server-type        :peer-server
             :access-key         "clojure_fullstack_key"
             :secret             "clojure_fullstack_secret"
             :endpoint           "localhost:8998"
             :validate-hostnames false})
```

**Note:** Refer to the table above to understand the values in this
map, as they were set in the bash command.

Then, create a client object using the datomic API

```clojure
(def client (d/client config))
```

Finally, connect the client to the `clojure_fullstack` database

```clojure
(def conn (d/connect client {:db-name "clojure_fullstack"}))
```

With these steps, you have effectively enabled a local connection between
Datomic's client and the peer server running in your machine. If you want
to connect to a remote server, just change the config map, and remember that
the server should be running a transactor
