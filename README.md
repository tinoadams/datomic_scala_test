datomic_scala_test
==================

This is an example using the Datomic database with Scala based on the Typesafe Activator "Hello Scala" template.

+ [First get Datomic](http://www.datomic.com/get-datomic.html)

+ Install Datomic following the instructions in the *Readme* file of the Datomic distribution.

+ Once Datomic is installed update the *Local Maven Repo* location and *libraryDependencies* in the build.sbt file according to your local installation:

        resolvers += "Local Maven Repository" at "file:///H:/.m2/repository"

        libraryDependencies += "com.datomic" % "datomic-free" % "0.8.4020.26"

+ Run the *Activator* using *activator.bat* or equivalent

For more information [check out the tutorial](http://docs.datomic.com/tutorial.html)

Here is a small [blog post](http://codebearing.blogspot.com.au/2013/07/basic-datomic-database-example-using.html) about the repo.
