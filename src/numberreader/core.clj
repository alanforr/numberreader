(ns numberreader.core
  (:require [clojure.math.numeric-tower :as nt :only '[expt]]))

(def numbers-dictionary
  "A hash-map that contains numbers as keys and string representations of those numbers as values."
  {0 "zero"
   1 "one"
   2 "two"
   3 "three"
   4 "four"
   5 "five"
   6 "six"
   7 "seven"
   8 "eight"
   9 "nine"
   10 "ten"
   11 "eleven"
   12 "twelve"
   13 "thirteen"
   14 "fourteen"
   15 "fifteen"
   16 "sixteen"
   17 "seventeen"
   18 "eighteen"
   19 "nineteen"
   20 "twenty"
   30 "thirty"
   40 "forty"
   50 "fifty"
   60 "sixty"
   70 "seventy"
   80 "eighty"
   90 "ninety"})

(def orders
  "A hash whose keys are numbers standing for orders of magnitude.
  The vals are the strings describing those order of magnitude,
  e.g for the key 3 the corresponding val is \"thousand\"."
  (let [get-name-order (fn [v] (clojure.string/lower-case (nth v 2)))
        get-order (fn [v]
                    (-> v
                    (second)
                    (#(drop 2 %))
                    (#(apply str %))
                    (read-string)))
        ls (clojure.string/split-lines (slurp "resources/orders.txt"))
        sls (map #(clojure.string/split % #"\t") ls)
        vs (map #(vector (get-order %) (get-name-order %)) sls)]
    (conj {2 "hundred" 3 "thousand"} (into {} vs))))

(defn magnitude-coefficient
  "Gives factor in front of terms of two given magnitudes.
  For example, (magnitude-coefficient 340594 100000 1000) would give 340."
  [number upper lower]
  (let [left (rem number upper)]
    (int (/ left lower))))

(defn magnitude-orders
  "Gives factor in front of terms of two given orders of magnitude."
  [number upperorder loworder]
  (magnitude-coefficient number (nt/expt 10 upperorder) (nt/expt 10 loworder)))

(defn tens-to-string
  "Takes a number in the range 0 to 99 and produces a string representing that number."
  [tes]
  (if (numbers-dictionary tes)
    (numbers-dictionary tes)
    (let
      [ones (numbers-dictionary (rem tes 10))
       ts (-> tes
              (#(/ % 10))
              (int)
              (#(* % 10))
              (numbers-dictionary))]
      (str ts "-" ones))))

(defn mag-to-string
  "Takes a number.
  Returns a blank string if zero and and the string (str (f mag) st) otherwise."
  [mag st f]
  (if (zero? mag)
    ""
    (str (f mag) st)))

(defn combine-lower-higher
  "Takes strings representing lower and higher numbers and combines them using a connector.
  If higher is \"one hundred\", lower is \"fifty-three\" and the connector is \" and \",
  then we get \"one hundred and fifty-three\"."
  [higher lower connector]
  (cond
    (empty? higher) lower
    (= "zero" lower) higher
    :else (str higher connector lower)))

(defn join-mags
  "Takes strings representing lower and higher numbers and combines them using a space."
  [s1 s2]
    (combine-lower-higher s1 s2 " "))

(defn out-of-range
  "Checks whether the number is greater than lower and less than upper.
  Throws an exception if the lower is larger than upper."
  [number lower upper]
  (if (> lower upper)
    (throw (Exception. "Your lower limit is larger than your upper limit."))
    (or (> number upper) (< number lower))))

(defn number-to-string
  "Converts a number to a gramatically correct string if it is between 0 and maximum,
  given functions for mapping the various orders of magnitude to strings
  and a function for joining the strings."
  [number maximum numstringfs joinfunc]
  (if (out-of-range number 0 maximum)
    (throw (Exception. (str "Your number is not between 0 and " maximum ".")))
    (let [mags ((apply juxt numstringfs) number)
          most (reduce joinfunc "" (butlast mags))]
      (combine-lower-higher (clojure.string/trim most) (last mags) " and "))))

(defn tens-hundreds-to-string
  "Works out strings for tens and hundreds and then joins them."
  [number]
  (number-to-string
    number
    999
    [(comp (fn [nm] (mag-to-string nm (str " " (orders 2)) numbers-dictionary))
           (fn [n] (magnitude-orders n 3 2)))
     (comp tens-to-string
           (fn [n] (magnitude-orders n 2 0)))]
    join-mags))

(defn string-function-generator [n]
  (comp (fn [y] (mag-to-string y (str " " (orders n)) tens-hundreds-to-string))
        (fn [x] (magnitude-orders x (+ n 3) n))))

(def number-string-functions
  (let [sorders (sort (keys orders))
        tes (comp tens-to-string #(magnitude-orders % 2 0))]
    (concat
      (mapv string-function-generator (reverse (drop 1 sorders)))
      [(comp (fn [nm] (mag-to-string nm (str " " (orders 2)) numbers-dictionary))
             (fn [n] (magnitude-orders n 3 2)))
       (comp tens-to-string
             (fn [n] (magnitude-orders n 2 0)))])))

(defn all-numbers-to-string
  [number]
  "Converts a number to a gramatically correct string if it is between 0 and 10^306-1 inclusive."
  (number-to-string
    number
    (-' (nt/expt 10 306) 1)
    number-string-functions
    join-mags))
