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
       (thousands-to-string 1) "one thousand"
       (thousands-to-string 831) "eight hundred and thirty-one thousand"))

(deftest tens-hundreds-to-string-test
  (are [x y] (= x y)
       (tens-hundreds-to-string 341) "three hundred and forty-one"
       (tens-hundreds-to-string 300) "three hundred"
       (tens-hundreds-to-string 0) "zero"))

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

(deftest up-to-mill-test
    (are [x y] (= x y)
       (up-to-mill 523) "five hundred and twenty-three"
       (up-to-mill 0) "zero"
       (up-to-mill 1000) "one thousand"
       (up-to-mill 12001) "twelve thousand and one"
       (up-to-mill 12041) "twelve thousand and forty-one"
       (up-to-mill 173345) "one hundred and seventy-three thousand three hundred and forty-five")
  (is (thrown? Exception #"Your number is not between 0 and 999999."
               (number-to-string -12)))
  (is (thrown? Exception #"Your number is not between 0 and 999999."
               (number-to-string 153000000))))
