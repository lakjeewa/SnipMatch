SnipMatch
=========

To build this project, you first need to checkout and build the main Eclipse Code
Recommenders project once. This installs the necessary parent POMs and related artifacts
into your local Maven repository (~/.m2/repository).

   git clone http://git.eclipse.org/gitroot/recommenders/org.eclipse.recommenders.git
   mvn clean install -f org.eclipse.recommenders/pom.xml

Thereafter, you can build this project by simply running

   mvn clean install

in the project's root directory.
