## Gradle Mongo Plugin ##

The Gradle Mongo Plugin allows you to run a managed instance of Mongo from your gradle build. It is based on the [Embedded MongoDb](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo) project by Flapdoodle OSS.

Plugin is available on [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22gradle-mongo-plugin%22). Nightly builds are available from the [Sonatype OSS Repo](https://oss.sonatype.org/content/repositories/snapshots/com/sourcemuse/gradle/plugin/gradle-mongo-plugin/).

### Usage ###

Enable the plugin in your gradle build by adding the jar to your buildscript dependencies:

```
buildscript {
   repositories {
      mavenCentral()
   }
   dependencies {
      classpath 'com.sourcemuse.gradle.plugin:gradle-mongo-plugin:0.9.0-SNAPSHOT'
   }
}

apply plugin: 'mongo'
```

Hey presto...

```
$ gradle tasks
...
Mongo tasks
-----------
startManagedMongoDb - Starts a local MongoDb instance which will stop when the build process completes
startMongoDb - Starts a local MongoDb instance
stopMongoDb - Stops the local MongoDb instance
...
```

### Configuration ###

Configure your Mongo instance inside a ```mongo``` block:

```
mongo {
    port 12345
    logging 'console'
    ...
}
```

The following properties are configurable:

* ```bindIp```: The ip address Mongo binds itself to (defaults to **'127.0.0.1'**)
* ```journalingEnabled```: Toggles journaling (defaults to **false**)
* ```logFilePath```: The desired log file path (defaults to **'embedded-mongo.log'**)
* ```logging```: The type of logging to be produced: **'console'**, **'file'** or **'none'** (defaults to **'file'**)
* ```mongodVerbosity```: The verbosity level of the mongod process. Supported options are as per the [mongod configuration documentation](http://docs.mongodb.org/manual/reference/program/mongod/#cmdoption--verbose) (default level is non-verbose)
* ```mongoVersion```: The version of Mongo to run. Can be **'DEVELOPMENT'** or **'PRODUCTION'** for the latest versions of the respective branch, or take the form **'1.2-LATEST'** or **'1.2.3'** for specific versions
* ```port```: The port Mongo will listen on (defaults to **27017**). For random port assignment, set this value to **'RANDOM'** (the actual port value used will be available during the build through the **project.mongo.port** property)
* ```storageLocation```: The directory location from where embedded Mongo will run, such as ```/tmp/storage``` (defaults to a java temp directory)
