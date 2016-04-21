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

(defn thousands
  "Gives the number of 1000s in number."
  [number]
  (int (/ number 1000)))

(defn hundreds
  "Gives the number of 100s in number."
  [number]
  (let [left (rem number 1000)]
    (int (/ left 100))))

(defn tens
  "Gives what is left over from number after the thousands and hundreds have been removed."
  [number]
  (rem number 100))

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

(defn hundreds-to-string
  "Takes a number in the range 0 to 9.
  Returns a blank string if zero and the string \"hs hundred\" otherwise."
  [hs]
  (if (zero? hs)
    ""
    (str (numbers-dictionary hs) " hundred")))

(defn thousands-to-string
  "Takes a number in the range 0 to 9.
  Returns a blank string if zero and and the string \"ths thousand\" otherwise."
  [ths]
  (if (zero? ths)
    ""
    (str (numbers-dictionary ths) " thousand")))

(defn combine-lower-higher
  "Takes strings representing lower and higher numbers and combines them using a connector.
  If higher is \"one hundred\", lower is \"fifty-three\" and the connector is \" and \",
  then we get \"one hundred and fifty-three\"."
  [higher lower connector]
  (cond
    (empty? higher) lower
    (= "zero" lower) higher
    :else (str higher connector lower)))

(defn out-of-range
  "Checks whether the number is greater than lower and less than upper.
  Throws an exception if the lower is larger than upper."
  [number lower upper]
  (if (> lower upper)
    (throw (Exception. "Your lower limit is larger than your upper limit."))
    (or (> number upper) (< number lower))))

(defn number-to-string
  "Converts a number to a gramatically correct string if it is between 0 and 1000."
  [number]
  (if (out-of-range number 0 1000)
    (throw (Exception. "Your number is not between 0 and 1000."))
    (let [ts (tens-to-string (tens number))
          hs (hundreds-to-string (hundreds number))
          ths (thousands-to-string (thousands number))
          ts-and-hs (combine-lower-higher hs ts " and ")]
      (combine-lower-higher ths ts-and-hs " "))))
