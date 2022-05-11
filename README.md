# java4ever-framework

[![JDK version](https://img.shields.io/badge/Java-17.0.2+-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/TON%20SDK-v1.33.0+-orange)](https://github.com/tonlabs/TON-SDK/tree/1.28.0)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)

**java4ever:framework** is a Java library for development, testing & DApps for
[Everscale](https://everscale.network/) network.
Framework is based (and dependant) on [java4ever-binding](https://github.com/deplant/java4ever-framework) library.

**Get quick help in our telegram
channel:** [![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/deplant\_chat)

### Goals

* Provide Java framework for Everscale development and testing
* Provide Java binding for EVER-SDK based on modern Java native memory access
* Support any modern versions of EVER-SDK without rebuild of binding itself
* Support custom EVER-SDK binaries

### Prerequisites

* **JDK 17** (17.0.2 or higher)
* **EVER-SDK** binary lib "**ton_client**" (build it yourself from github or get [precomiled ones](https://github.com/tonlabs/ever-sdk/blob/master/README.md#download-precompiled-binaries))
* **java.library.path** set. Add correct path to library as argument to Java run: `-Djava.library.path=<path_to_ton_client>`.


### Download

#### Gradle

```groovy
dependencies {
    implementation 'tech.deplant.java4ever:java4ever-binding:1.1.2'
    implementation 'tech.deplant.java4ever:java4ever-framework:1.1.2'    
}
```

#### Maven

```xml
<dependency>
    <groupId>tech.deplant.java4ever</groupId>
    <artifactId>java4ever-binding</artifactId>
    <version>1.1.2</version>
</dependency>
<dependency>
    <groupId>tech.deplant.java4ever</groupId>
    <artifactId>java4ever-framework</artifactId>
    <version>1.1.2</version>
</dependency>
```

### Can be used with any TON-SDK version

You can use TON-SDK 1.33.0+ for your project or even load multiple libraries with different versions. You can use your
custom TON-SDK fork if you like. You can use even lower versions, but we will not fix any issues with it.