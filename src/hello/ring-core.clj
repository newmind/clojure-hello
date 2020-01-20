; http://clojure.or.kr/wiki/doku.php?id=lecture:ring:docs
(ns hello-world.core)

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})

(use 'ring.adapter.jetty)
(use 'hello-world.core)
; (run-jetty handler {:port 3000})