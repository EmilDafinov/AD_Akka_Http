# AD_Akka_Http

[![Build Status](https://travis-ci.org/emiliorodo/AD_Akka_Http.svg?branch=master)](https://travis-ci.org/emiliorodo/AD_Akka_Http)

Creating a sample full stack web-app using Scala and AngularJS

Requires Java 8

Development environment
=======================

Building the application
------------------------
This project is built and packaged using SBT.
You can install sbt with your favorite package manager, under OS X
I would recommend Homebrew

```
brew install sbt
```

The build is configured in the ```build.sbt``` file found in the root
directory of the project


Data store
----------
The application uses a Postgres instance as its data store. In order to 
create a freash instance for testing/demo pursposes, you can use Docker. 
The Dockerfile for the db can be found in the ```./database```
under the root of this project.
Download Docker toolbox [here](https://www.docker.com/products/docker-toolbox) and install it 

You can build the image by running

```
docker build -t emiliorodo/ad_db .
```

while in the /database directory.
 
When the image is built, you can create a db container with the

```
docker run --name AD_DB -p 32771:5432 -e POSTGRES_PASSWORD=qwerty -d emiliorodo/ad_db
```

This would create a container named AD_DB on your docker host. 
```
echo $DOCKER_HOST
```
would give the IP of your docker VM.
The db would be running on port 32771. However you can map it to a different
port if you'd like.

Configuration
-------------
The application configuration can be found in 
```
./src/main/resources/application.conf
```
Many aspects of the application can be configured there. 
Read the comments in that file for more information.

Packaging
---------
The application is packaged as a runnable fat jar using the sbt-assembly 
plugin. The class containing the applicaiton's main method, as well as 
the name and location of the output jar can be configured in build.sbt

```
sbt assembly
```
in the root directory of the project. This will package the application, 
along with all resources and dependencies into a single runnable .jar
file. Look ath the console output to see where the jar is located.

Running the application
-----------------------

You can run the application by simply running the jar you have obtained during the
previous step:

```
java -jar <name-of-packaged-jar>
```

AppDirect event routes
----------------------

The rest interface of the application supports the SUBSCRIPTION_ORDER
and SUBSCRIPTION_CANCEL events. The endpoint URLS for these are

SUBSCRIPTION_ORDER:

{applicationHost}:{applicationPort}/ad/events/subscription/order?eventUrl={eventUrl}

SUBSCRIPTION_CANCEL:

{applicationHost}:{applicationPort}/ad/events/subscription/cancel?eventUrl={eventUrl}
