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

; http://clojure.or.kr/wiki/doku.php?id=lecture:clojure:%EC%8B%9C%EC%9E%91
(defn countdown [x]
  (if (zero? x)
    :blastoff2!
    (do (println x)
        (recur (dec x)))))
(countdown 5)

; http://clojure.or.kr/wiki/doku.php?id=lecture:clojure:destructuring
(def v [42 "foo" 99.2 [5 12]])
(+ (first v) (v 2))


; https://clojure.org/guides/repl/enhancing_your_repl_workflow
;;;; 1. NOT REPL-friendly
;; We won't be able to change the way numbers are printed without restarting the REPL.
; (future
;   (run!
;    (fn [i]
;      (println i "green bottles, standing on the wall. ♫")
;      (Thread/sleep 1000))
;    (range 10)))

;;;; 2. REPL-friendly
;; We can easily change the way numbers are printed by re-defining print-number-and-wait.
;; We can even stop the loop by having print-number-and-wait throw an Exception.


(defn print-number-and-wait
  [i]
  (prn i "green bottles, standing on the wall. ♫")
  (Thread/sleep 1000))

(future
  (run!
   (fn [i] (print-number-and-wait i))
   (range 0 5)))