(ns numberreader.core)

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

(defn magnitude-coefficient
  "Gives factor in front of terms of two given magnitudes.
  For example, (magnitude-coefficient 340594 100000 1000) would give 340."
  [number upper lower]
  (let [left (rem number upper)]
    (int (/ left lower))))

(defn thousands
  "Gives the number of 1000s in number."
  [number]
  (magnitude-coefficient number 1000000 1000))

(defn hundreds
  "Gives the number of 100s in number."
  [number]
  (magnitude-coefficient number 1000 100))

(defn tens
  "Gives what is left over from number after the thousands and hundreds have been removed."
  [number]
  (magnitude-coefficient number 100 1))

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
  Returns a blank string if zero and and the string (str mag st) otherwise."
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

(defn hundreds-to-string
  "Takes a number in the range 0 to 9.
  Returns a blank string if zero and the string \"hs hundred\" otherwise."
  [hs]
  (mag-to-string hs " hundred" numbers-dictionary))

(defn out-of-range
  "Checks whether the number is greater than lower and less than upper.
  Throws an exception if the lower is larger than upper."
  [number lower upper]
  (if (> lower upper)
    (throw (Exception. "Your lower limit is larger than your upper limit."))
    (or (> number upper) (< number lower))))

(defn join-mags
  [s1 s2]
    (combine-lower-higher s1 s2 " "))

(defn number-to-string
  "Converts a number to a gramatically correct string if it is between 0 and maximum,
  given functions for napping the various orders of magnitude to strings."
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
    [(comp hundreds-to-string hundreds)
     (comp tens-to-string tens)]
    join-mags))

(defn thousands-to-string
  "Takes a number in the range 1 to 999 converts it with tens-hundreds-to-string
  and adds the string \" thousand\" to the end."
  [ths]
  (mag-to-string ths " thousand" tens-hundreds-to-string))

(defn up-to-mill
  "Converts a number to a gramatically correct string if it is between 0 and 1 million-1 inclusive."
  [number]
  (number-to-string
    number
    999999
    [(comp thousands-to-string thousands)
     (comp hundreds-to-string hundreds)
     (comp tens-to-string tens)]
    join-mags))
