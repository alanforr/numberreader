# numberreader

A Clojure library designed to read a number between 0 and 1000 inclusive, and translate it into a gramatically correct representation.

For example, the library would translate 111 into "one hundred and eleven" and 64 into "sixty-four". The output is given as a string.

If the number is not between 0 and 1000 inclusive, an exception will be thrown.

Documentation can be found in target/doc/ and was generated using [codox](https://github.com/weavejester/codox).

## Usage

Download the library, navigate to the top level, i.e. - the directory containing project.clj and start the REPL.

In the REPL, type (require '[numberreader.core :refer :all]), then type (number-to-string number) for whatever number you like.

Typical output is shown below:

=> (number-to-string 523)
"five hundred and twenty-three"

=>(number-to-string 1000)
"one thousand"

=> (number-to-string -12)
Exception Your number is not between 0 and 1000.  numberreader.core/number-to-string (core.clj:91)

## License

Copyright © 2016 Alan Forrester

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
