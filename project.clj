(defproject cerberus "0.1.0-SNAPSHOT"
  :description "A distributed loadbalancing proxy"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                  [aleph "0.3.2"]
                  [compojure "1.1.6"]
                  [ring-server "0.3.0"]
                  [sonian/carica "1.1.0"]
                  [com.duelinmarkers/ring-request-logging "0.2.0"]]
   :main cerberus.core)

