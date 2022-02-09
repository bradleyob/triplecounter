Triple Counter
------------
Provide a list of files to count the recurring triples
in each file: `scala triplecounter.jar <file1> ... <fileN>`
The counter also accepts streamed input from STDIN : `cat <file> | scala triplecounter.jar`


SETUP
-------------
This project runs on Scala 2.13.8
To install and run, get Homebrew (https://docs.brew.sh/Installation)
Once installed, run `brew install scala` (at time of writing the latest Scala version 2.13.8 will be installed
SBT 1.3.13 is also required, run `brew install sbt`

After both sbt and Scala are installed, execute `sbt compile package` to generate the triplecounter.jar binary.

####Packaged JAR


Alternatively, a jar with full Scala dependencies may be generated with `sbt assembly`

EXECUTION
-------------
Run `scala triplecounter.jar -h` to view the above help documentation.


####Using sbt assembly packaged jar and Java -jar


If you have chosen to compile a Java compatible jar, you may run `java -jar triplecounter.jar -h` to view output.

`java -jar triplecounter.jar <file1>..<fileN>` may be used to execute with a filepath.

`cat <filename> | java -jar triplecounter.jar` may be used for input via STDIN.


TEST
-------------
Tests may be executed with `sbt test`
They may be viewed in src/test/scala/MainTest.scala

CAVEATS
-------------
Preferably I would refactor this to not persist state to the main object and create a prettier layout for the results.
Additionally support for different output formats (csv, json) would be nice.
Support for processing infinite streams and storing results in some other persistence layer would be the next logical
    steps for this application.


