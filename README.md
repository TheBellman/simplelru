# SimpleLRU
A simple least-recently-used cache, where the cache is capped at a certain size, and oldest entries are ejected when the cache becomes full.

This project builds a JAR which can then be used within your projects.

## Building

The following notes all assume that you have access to the command line and know what to do there, have a fairly recent version of Maven, and at least Java 7. The code will build with Java 8, but is pegged to Java 7 compliance. 

To build the  JAR after checking out the project:

```
git clone https://github.com/TheBellman/simplelru.git
cd simplelru
mvn clean package
```

After a bit of grinding, the JAR should be available at

```
target/SimpleLRU-1.0-SNAPSHOT.jar 
```

A site report can be built locally as well, which will provide you with JavaDoc and test coverage details:

```
mvn site
```

This will result in a local web site that can be accessed via

```
target/site/index.html
```
**Note:** A current release version can be found in my private Artifactory at the URL below. There are instructions there for how to include this repository in your build cycle.

```
http://54.209.160.169:8081/artifactory/webapp/#/home
```

## Use

There are two cache implementations, *SimpleLRUCache* and *TimedLRUCache*, which at some point in the future I may refactor to share a common interface. Both of these are intended to be highly performant and thread safe, however it is up to you to manage the number of cache instances in your application.

*SimpleLRUCache* is the simplest to think about initially. Let's take the simple case of a cache keyed by *String* with *Integer* values, capped to have at most 100 items:

```
SimpleLRUCache<String, Integer> cache = new SimpleLRUCache<>(100);
```
you can then add to the cache by calling *put* (Note we are autoboxing the integer), and retrieve with *get*:

```
cache.put("MyKey", 123456);
Integer myNum = cache.get("MyKey");
```
Which is about all there is to it. Note that if the requested value is not in the cache, a *null* is returned. 

Refer to the JavaDoc to see the other methods on this class, which are provided should you need to manage the contents of the cache manually.

*TimedLRUCache* appears to have very similar semantics, with the addition of an additional constructor parameter:

```
TimedLRUCache<String, Integer> cache = new TimedLRUCache<>(100, 60000);

cache.put("MyKey", 123456);
Integer myNum = cache.get("MyKey");
```
The key difference is that any item placed in the cache will only survive for a certain time (in this case 1 minute) before evaporating into the aether.

Most of the remaining methods for this class are also provided to help with managing the content of the cache, but *touchAndGet* deserves special mention. It behaves just like *get*:

```
Integer myNum = cache.touchAndGet("MyKey");
```
except if the value is present in the cache, it's count-down clock is reset.

The remaining class in the package, *TimedHolder*, may be of some use to you, however it's primarily there to support *TimedLRUCache*. It is a thread safe container for an object where the container is emptied after a certain time. As with any container if you obtain the content of the *TimedHolder* in one thread, and then set the content to something different in a second thread, the first thread retains whatever content it found, rather than "seeing" the new content unless it re-fetches.