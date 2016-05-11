(ns numberreader.core-test
  (:require [clojure.test :refer :all]
            [numberreader.core :refer :all]
            [clojure.math.numeric-tower :as nt :only '[expt]]))

(deftest magnitude-coefficient-test
  (are [x y] (= x y)
       (magnitude-coefficient 321156 1000000 1000) 321
       (magnitude-coefficient 56 1000000 1000) 0
       (magnitude-coefficient 1431780 1000000 1000) 431))

(deftest tens-to-string-test
  (are [x y] (= x y)
       (tens-to-string 1) "one"
       (tens-to-string 31) "thirty-one"))

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

(deftest all-numbers-to-string-test
    (are [x y] (= x y)
       (all-numbers-to-string 523) "five hundred and twenty-three"
       (all-numbers-to-string 0) "zero"
       (all-numbers-to-string 1000) "one thousand"
       (all-numbers-to-string 12001) "twelve thousand and one"
       (all-numbers-to-string 12041) "twelve thousand and forty-one"
       (all-numbers-to-string 173345) "one hundred and seventy-three thousand three hundred and forty-five"
       (all-numbers-to-string 123456789101112)
         "one hundred and twenty-three trillion four hundred and fifty-six billion seven hundred and eighty-nine million one hundred and one thousand one hundred and twelve"
       (all-numbers-to-string 1000000000000) "one trillion"
       (all-numbers-to-string 1000000000012) "one trillion and twelve")
  (is (thrown? Exception #"Your number is not between 0 and 999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999."
               (number-to-string -12)))
  (is (thrown? Exception #"Your number is not between 0 and 999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999."
               (number-to-string (nt/expt 10 307)))))
