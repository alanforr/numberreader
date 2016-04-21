(ns numberreader.core-test
  (:require [clojure.test :refer :all]
            [numberreader.core :refer :all]))

(deftest thousands-test
  (are [x y] (= x y)
       (thousands 1000) 1
       (thousands 2430) 2
       (thousands 250) 0
       (thousands 35) 0
       (thousands 3) 0))

(deftest hundreds-test
  (are [x y] (= x y)
       (hundreds 5000) 0
       (hundreds 3520) 5
       (hundreds 230) 2
       (hundreds 93) 0
       (hundreds 6) 0))

(deftest tens-test
  (are [x y] (= x y)
       (tens 3400) 0
       (tens 7630) 30
       (tens 305) 5
       (tens 43) 43
       (tens 4) 4))

(deftest tens-to-string-test
  (are [x y] (= x y)
       (tens-to-string 53) "fifty-three"
       (tens-to-string 10) "ten"))

(deftest hundreds-to-string-test
  (are [x y] (= x y)
       (hundreds-to-string 0) ""
       (hundreds-to-string 3) "three hundred"))

(deftest thousands-to-string-test
  (are [x y] (= x y)
       (thousands-to-string 0) ""
       (thousands-to-string 1) "one thousand"))

(deftest combine-lower-higher-test
  (are [x y] (= x y)
       (combine-lower-higher "three hundred"  "ten" " and ") "three hundred and ten"
       (combine-lower-higher "seven hundred"  "zero" " and ") "seven hundred"
       (combine-lower-higher "one thousand" "six hundred" " ") "one thousand six hundred"))

(deftest out-of-range-test
  (are [x y] (= x y)
       (out-of-range 10 0 1000) false
       (out-of-range 5 7 34) true
       (out-of-range 345 62 250) true)
  (is (thrown? Exception #"Your lower limit is larger than your upper limit."
               (out-of-range 1 5 2))))

(deftest number-to-string-test
  (are [x y] (= x y)
       (number-to-string 523) "five hundred and twenty-three"
       (number-to-string 0) "zero"
       (number-to-string 1000) "one thousand")
  (is (thrown? Exception #"Your number is not between 0 and 1000."
               (number-to-string -12)))
  (is (thrown? Exception #"Your number is not between 0 and 1000."
               (number-to-string 1530))))
