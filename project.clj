(defproject cerberus "0.1.0-SNAPSHOT"
    :description "A distributed loadbalancing proxy"
    :url "http://example.com/FIXME"
    :license {:name "Eclipse Public License"
              :url "http://www.eclipse.org/legal/epl-v10.html"}
    :dependencies [[org.clojure/clojure "1.6.0"]
                  [midje "1.6.3"]
                  [compojure "1.1.6"]
                  [ring-server "0.3.0"]
                  [ring "1.3.0-RC1"]
                  [sonian/carica "1.1.0"]
                  [clj-http "0.9.1"]
                  [org.zeromq/jeromq "0.3.4"]
                  [com.stuartsierra/component "0.2.1"]
                  [com.duelinmarkers/ring-request-logging "0.2.0"]]
    :plugins [[lein-ring "0.8.7"]
              [lein-midje "3.1.1"]]
    :test-paths ["test"]
    :ring {:handler cerberus.core/app})

