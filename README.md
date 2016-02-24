# simplelru
Simple least-recently-used cache, where the cache is capped at a certain size, and oldest entries are ejected when the cache becomes full.

This project builds a JAR which can then be used within your projects.

## Building

To build the  JAR after checking out the project:

```
  mvn clean package
```

After a bit of grinding, the JAR should be available at

```
  target/SimpleLRU-1.0-SNAPSHOT.jar 
```

A site report can be built locally as well, which will provide you with java doc and test coverage details:

```
  mvn site
```

This will result in a local web site that can be accessed via

```
  target/site/index.html
```

## Use

There are two ... _please come back tomorrow, this is a work in progress_
