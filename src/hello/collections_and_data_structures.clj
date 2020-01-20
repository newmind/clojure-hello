(ns hello.core)

; http://clojure.or.kr/wiki/doku.php?id=lecture:clojure:collections_and_data_structures
;;;;;;;;;;;;


(let [s (range 1e6)]
  (time (count s)))
;; "Elapsed time: 58.6274 msecs"
;; => 1000000

(let [s (apply list (range 1e6))]
  (time (count s)))
;; "Elapsed time: 0.016 msecs"
;; => 1000000

; clojure.lang.ISeq 인터페이스를 구현한 것
(seq? '(1 2))      ;=> true
(seq? [1 2])       ;=> false
(seq? {:foo :bar}) ;=> false
(seq? #{1 2})      ;=> false
(seq? ())          ;=> true

(class '(1 2))      ;=> clojure.lang.PersistentList
(class [1 2])       ;=> clojure.lang.PersistentVector
(class {:foo :bar}) ;=> clojure.lang.PersistentArrayMap
(class #{1 2})      ;=> clojure.lang.PersistentHashSet
(class ())          ;=> clojure.lang.PersistentList$EmptyList

(seq '(1 2))      ;=> (1 2)
(seq [1 2])       ;=> (1 2)
(seq {:foo 'foo, :bar 'bar}) ;=> ([:foo :bar])
(seq #{1 2})      ;=> (1 2)
(seq ())          ;=> nil

(class (seq '(1 2)))      ;=> clojure.lang.PersistentList
(class (seq [1 2]))       ;=> clojure.lang.PersistentVector$ChunkedSeq
(class (seq {:foo :bar})) ;=> clojure.lang.PersistentArrayMap$Seq
(class (seq #{1 2}))      ;=> clojure.lang.PersistentHashSet$KeySeq
(class (seq ()))          ;=> nil


;; 레이지 (Lazy) 시퀀스

(defn random-ints [limit]
  "Returns a lazy seq of random integers in the range [0,limit)."
  (lazy-seq
   (println "realizing random number")
   (cons (rand-int limit)
         (random-ints limit))))

(def rands (take 10 (random-ints 50)))  ;;; 1.
;= #'user/rands
(first rands)                           ;;; 2.
; realizing random number
;= 39
(first rands)                           ;;; 3.
;= 39
(second rands)                          ;;; 4.
; realizing random number
;= 17
(nth rands 3)                           ;;; 5.
; realizing random number
; realizing random number
;= 44
(count rands)                           ;;; 6.
; realizing random number
; realizing random number
; realizing random number
; realizing random number
; realizing random number
;= 10
(count rands)                           ;;; 7.
;= 10

; 접근시마다 2개의 값을 평가하도록 random-ints를 수정한 것이다
(defn random-ints [limit]
  "Returns a lazy seq of random integers in the range [0,limit)."
  (lazy-seq
   (println "realizing random number")
   (list* (rand-int limit) (rand-int limit)
          (random-ints limit))))

(def rands (take 10 (random-ints 50)))
;= #'user/rands
(first rands)                      ;; 1. 
; realizing random number
;= 16
(first rands)                      ;; 2.
;= 16
(second rands)                     ;; 3.
;= 33
(nth rands 2)                      ;; 4.
; realizing random number
;= 47
(nth rands 3)                      ;; 5.
;= 5


(repeatedly 10 (partial rand-int 50))
;=> (47 19 26 16 39 5 30 13 9 43)

(defn random-ints [limit]
  "Returns a lazy seq of random integers in the range [0,limit)."
  (lazy-seq
   (println "realizing random number")
   (repeatedly 10 (partial rand-int limit))))

(def x (next (random-ints 50)))
(def x (rest (random-ints 50)))

(let [[x & rest] (random-ints 50)])

(dorun (take 5 (random-ints 50)))

; 머리 잡기 (Head retention)
(split-with neg? (range -5 5))

; (let [[t d] (split-with #(< % 12) (range 1e8))]
;   [(count d) (count t)])    ;= #<OutOfMemoryError java.lang.OutOfMemoryError: Java heap space>

; (let [[t d] (split-with #(< % 12) (range 1e8))]
;   [(count t) (count d)])    ;= [12 99999988]


;; 연합 추상

(def m {:a 1, :b 2, :c 3})
;= #'user/m
(get m :b)
;= 2
(get m :d)
;= nil
(get m :d "not-found")
;= "not-found"
(assoc m :d 4)
;= {:a 1, :b 2, :c 3, :d 4}
(dissoc m :b)
;= {:a 1, :c 3}
(assoc m
       :x 4
       :y 5
       :z 6)
;= {:z 6, :y 5, :x 4, :a 1, :c 3, :b 2}
(dissoc m :a :c)
;= {:b 2}

(def v [1 2 3])
;= #'user/v
(get v 1)
;= 2
(get v 10)
;= nil
(get v 10 "not-found")
;= "not-found"
(assoc v
       1 4
       0 -12
       2 :p)
;= [-12 4 :p]

(contains? [1 2 3] 0)
;= true
(contains? {:a 5 :b 6} :b)
;= true
(contains? {:a 5 :b 6} 42)
;= false
(contains? #{1 2 3} 1)
;= true
(contains? [1 2 3] 3)
;= false
(contains? [1 2 3] 2)
;= true
(contains? [1 2 3] 0)
;= true

(get {:ethel nil} :lucy)
;= nil
(get {:ethel nil} :ethel)
;= nil
(find {:ethel nil} :lucy)
;= nil
(find {:ethel nil} :ethel)
;= [:ethel nil]