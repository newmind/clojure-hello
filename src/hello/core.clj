(ns hello.core
  (:require
   [taoensso.timbre :as timbre
    :refer [log  trace  debug  info  warn  error  fatal  report
            logf tracef debugf infof warnf errorf fatalf reportf
            spy get-env]]))

(defn -main 
  [] 
  (println "안녕하세요 Hello World"))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World! 3"))

; https://medium.com/appsflyer/repl-based-debugging-in-clojure-278fb468a33
(defn divide [a b]
  (/ a b))

(defn my-calc [a b c]
  (let [plus (+ a b c)
        minus (- a b c)
        divide (/ a b c)]
    (+ plus minus divide)))

(timbre/info "this will print an info!")
(timbre/debug "this will print debug!")

(def x {:a 1 :b 2})
(timbre/debug x)

(timbre/warn "Invalid message format, msg is: " x)
(timbre/spy (my-calc 1 2 3))

; https://github.com/ptaoussanis/timbre  timbre log
(info "This will print")
(spy :info (* 5 4 3 2 1))
(defn my-mult 
  [x y] 
  (info "Lexical env:" (get-env)) (* x y))
(my-mult 4 7)
(trace "This won't print due to insufficient log level")
