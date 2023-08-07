(ns backend.database.schemas.core
  (:require
    [backend.database.schemas.task :as task]
    [backend.database.schemas.user :as user]))

(def schemas (concat
               task/task-schema
               user/user-schema))
