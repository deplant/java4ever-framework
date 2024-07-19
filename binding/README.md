# EVER-SDK for Java

[![JDK version](https://img.shields.io/badge/Java-22-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/EVER%20SDK-v1.45.0-orange)](https://github.com/tonlabs/ever-sdk)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)

* Discuss in
  Telegram: [![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/deplant\_chat\_en)
* Read full
  docs: [![javadoc](https://javadoc.io/badge2/tech.deplant.java4ever/java4ever-binding/javadoc.svg)](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-binding)

This is a Java binding project for
[EVER-SDK](https://github.com/tonlabs/ever-sdk) library that is 
compatible with 
[Everscale](https://everscale.network/), Venom, GOSH & TON blockchain 
networks.
Binding calls [JSON-RPC](https://github.com/tonlabs/ever-sdk/blob/master/docs/for-binding-developers/json_interface.md) interface of EVER-SDK.
Native calls are based on modern [Foreign Function & Memory API](https://openjdk.org/jeps/454).

This artifact provides full binding functionality, but doesn't include 
higher level helpers for development, tests or fast prototyping. Try our larger [Java4Ever](https://github.com/deplant/java4ever-framework) framework 
that is based on this binding for easier work with TVM blockchains.

### Goals

* Provide Java binding for EVER-SDK based on modern Java native memory access
* Support any modern versions of EVER-SDK without rebuild of binding itself
* Support custom EVER-SDK binaries

## Quick start

#### Prerequisites

* Install **JDK 22** ([link](https://adoptium.net/temurin/releases?version=20))

#### Add java4ever to your Maven of Gradle setup:

* Gradle

```groovy
dependencies {
    implementation 'tech.deplant.java4ever:java4ever-binding:3.1.2'
}
```

* Maven

```xml

<dependency>
    <groupId>tech.deplant.java4ever</groupId>
    <artifactId>java4ever-binding</artifactId>
    <version>3.1.2</version>
</dependency>
```

## Examples and Guides

### Setting up SDK

**SDK** setup consists of two steps:
1. Loading EVER-SDK library (should be done once)
2. Creating context/session with certain config (should be done for every new endpoint or config change)

Both steps are described below.

#### Loading EVER-SDK library

To load EVER-SDK connection to JVM, use `EverSdk.load()` static method.
Loaded EVER-SDK is a singleton, you can't use other version of library simultaneously.
Java4Ever stores wrapped copy of actual EVER-SDK libraries in its resources. To load wrapped library, run:
```java
EverSdk.load();
```
**Note: We found problems with loading library from resources using Spring's fatJar bundles. Please, use alternative loaders if you use fatJar too.**

If you want to use custom binaries or version, you should use other loaders.
All loaders are just ways to reach library, so you should get/build `ton_client` library first.
You can find EverX [precompiled EVER-SDK files](https://github.com/tonlabs/ever-sdk/blob/master/README.md#download-precompiled-binaries) here.
Here are the examples of loader options:
```java
// loads library from path saved in environment variable
EverSdk.load(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB")); 
// loads library from ~ (user home)
EverSdk.load(AbsolutePathLoader.ofUserDir("libton_client.so")); 
// loads from any absolute path
EverSdk.load(new AbsolutePathLoader(Path.of("/home/ton/lib/libton_client.so"))); 
// loads library from java.library.path JVM argument
EverSdk.load(new JavaLibraryPathLoader("ton_client")); 
```

#### Creating config context and specifying endpoints

Context configuration is needed to provide EVER-SDK library with your endpoints, timeouts and other settings.
You can find a list of endpoints here: https://docs.evercloud.dev/products/evercloud/networks-endpoints
If you're working with Everscale mainnet, here you can register your app and receive "ProjectID" part of the URL: https://dashboard.evercloud.dev/

```java
// creates default EVER-SDK context without specifying endpoints
int contextId1 = EverSdk.createDefault(); 
// creates default EVER-SDK with specified endpoint
int contextId2 = EverSdk.createWithEndpoint("http://localhost/graphql"); 
// creates EVER-SDK context from ready JSON string
int contextId4 = EverSdk.createWithJson(configJsonString);
```

Save your contextId, you will use this id to call EVER-SDK methods.

#### Configuring SDK with Builder

Alternatively, you can call EverSdk.builder() that provides builder methods for all config values of EVER-SDK.
Thus you can easily configure only needed parts of library.
```java
int contextId3 = EverSdk.builder()
                       .networkEndpoints("http://localhost/graphql")
                       .networkQueryTimeout(300_000L)
                       .build();
```

### Calling EVER-SDK methods

It's very simple, just type ModuleName.methodName (list of modules and methods is here: [EVER-SDK API Summary](https://github.com/tonlabs/ever-sdk/blob/master/docs/SUMMARY.md) ). 
Note that method names are converted from snake_case to camelCase. Then pass EverSdkContext object as 1st parameter. That's all.

```java
int contextId = TestEnv.newContextEmpty();
var asyncResult = Client.version(contextId);
var syncResult = EverSdk.await(asyncResult);
System.out.println("EVER-SDK Version: " + syncResult.version());
```


## Notes

### Custom EVER-SDK libs

EVER-SDK libs are included in the distribution, but if you want to use custom one - build **EVER-SDK** binary lib "**ton_client**"(.so/.dll) yourself (or
  get [precompiled one](https://github.com/tonlabs/ever-sdk/blob/master/README.md#download-precompiled-binaries))


### Logging

java4ever-binding uses the JDK Platform Loggging (JEP 264: Platform Logging API and Service),
so can be easily bridged to any logging framework.