(ns hello.core
  (:require
   [taoensso.timbre :as timbre
    :refer [log  trace  debug  info  warn  error  fatal  report
            logf tracef debugf infof warnf errorf fatalf reportf
            spy get-env]]))

;;;; http://clojure.or.kr/wiki/doku.php?id=lecture:clojure:destructuring
;; 인수분해 Destructuring

;;;-----------------------------------------
;;; 시퀀스 인수분해

(def v [42 "foo" 99.2 [5 12]])
;; => #'hello.core/v

(+ (first v) (v 2))
;; => 141.2

(+ (first v) (first (last v)))
;; => 47

(let [[x y z] v]
  (+ x z))
;; => 141.2
(let [x (nth v 0)
      y (nth v 1)
      z (nth v 2)]
  (+ x z))
;; => 141.2

(let [[x _ _ [y z]] v] (+ x y z))
;; => 59

(let [[x & rest] v] rest)
;; => ("foo" 99.2 [5 12])


(let [[x _ z :as org] v] (conj org (+ x z)))
;; => [42 "foo" 99.2 [5 12] 141.2]

(defn f [] [1 2 3])
(let [[x y] (f)]
  (conj (f) (+ x y)))
;; => [1 2 3 3]
(let [[x y :as all] (f)]
  (conj all (+ x y)))
;; => [1 2 3 3]

(let [[a b c & more :as all] (range 10)]
  (println "a b c are: " a b c)
  (println "more is: " more)
  (println "all is: " all))
;; => a b c are:  0 1 2
;; => more is:  (3 4 5 6 7 8 9)
;; => all is:  (0 1 2 3 4 5 6 7 8 9)
;; => nil

;;;-----------------------------------------
;;; 맵 인수분해

(def m {:a 5 :b 6
        :c [7 8 9]
        :d {:e 10 :f 11}
        "foo" 88
        42 false})
;; => #'hello.core/m

(let [{a :a b :b} m]
  (+ a b))
;; => 11

; 맵 인수분해는 키별 분해
(let [{f "foo"} m]
  (+ f 12))
;; => 100

(let [{f 42} m]
  (if f 1 0))
;; => 0

; 벡터에 대한 맵 인수분해(인덱스)
(let [{x 3 y 8} [12 0 0 -18 44 6 0 0 1]]
  (+ x y))
;; => -17

(do
  (let [{x 3 y 8} [12 0 0 -18 44 6 0 0 1]]
    (pr x y)
    (+ x y)))
;; => -18 1
;; => -17

; 내부 맵 인수분해
(let [{{e :e} :d} m]
  (* 2 e))
;; => 20

; 시퀀스 인수분해와 맵 인수분해 같이 사용하기
(let [{[x _ y] :c} m]
  (+ x y))
;; => 16

(def map-in-vector ["James" {:birthday (java.util.Date. 73 1 6)}])
(let [[name {bd :birthday}] map-in-vector]
  (str name " was born on " bd))
;; => "James was born on Tue Feb 06 00:00:00 KST 1973"

; 원본 인수분해
(let [{r1 :x r2 :y :as randoms}
      (zipmap [:x :y :z] (repeatedly (partial rand-int 10)))]
  (assoc randoms :sum (+ r1 r2)))
;; => {:x 4, :y 8, :z 2, :sum 12}

; 기본값 설정
(let [{k :unknown x :a
       :or {k 50}} m]
  (+ k x))
;; => 55
(let [{k :unknown x :a} m
      k (or k 50)]
  (+ k x))
;; => 55

; :or는 피인수분해의 해당 키 값이 false이거나 nil일 때도 동작한다.
(let [{opt1 :option} {:option false}
      opt1 (or opt1 true)
      {opt2 :option :or {opt2 false}} {:option true}] ; 
  {:opt1 opt1 :opt2 opt2})
;; => {:opt1 true, :opt2 true}

; 맵키 이름 인수분해
(def kildong {:name "KilDong" :age 24 :location "west"})
(let [{name :name age :age location :location} kildong]
  (format "%s is %s years old and lives in %s." name age location))
;; => "KilDong is 24 years old and lives in west."
(let [{:keys [name age location]} kildong]
  (format "%s is %s years old and lives in %s." name age location))
;; => "KilDong is 24 years old and lives in west."

(def kildong {"name" "KilDong" "age" 24 "location" "west"})
(let [{:strs [name age location]} kildong]
  (format "%s is %s years old and lives in %s." name age location))
;; => "KilDong is 24 years old and lives in west."

(def kildong {'name "KilDong" 'age 24 'location "west"})
(let [{:syms [name age location]} kildong]
  (format "%s is %s years old and lives in %s." name age location))
;; => "KilDong is 24 years old and lives in west."

; 나머지 시퀀스를 키-값 쌍으로 인수분해
(def movie ["Les Miserables" 2012 :director "Tom Hooper" :rating 8.0])
(let [[movie-name year & rest] movie
      {:keys [director rating]} (apply hash-map rest)]
  (format "%s is made by %s in %s, rating %.1f" movie-name year director rating))
;; => "Les Miserables is made by 2012 in Tom Hooper, rating 8.0"
(let [[movie-name year & {:keys [director rating]}] movie]
  (format "%s is made by %s in %s, rating %s" movie-name year director rating))
;; => "Les Miserables is made by 2012 in Tom Hooper, rating 8.0"

(let [[a & r] #{1 2 3}] r)
;; => (3 2)

(let [[a b & r] m] a)
;; => [:a 5]
(let [[a b & r] m] b)
;; => [:b 6]
(let [[a b & r] m] r)
;; => ([:b 6] [:c [7 8 9]] [:d {:e 10, :f 11}] ["foo" 88] [42 false])
