# java4ever-framework

[![javadoc](https://javadoc.io/badge2/tech.deplant.java4ever/java4ever-framework/javadoc.svg)](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-framework)
[![JDK version](https://img.shields.io/badge/Java-19+-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/TON%20SDK-v1.37+-orange)](https://github.com/tonlabs/TON-SDK/tree/1.28.0)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)

**java4ever:framework** is a Java 19 framework for development, testing & DApps for
[Everscale](https://everscale.network/) network.
Framework is based (and dependant) on [java4ever-binding](https://github.com/deplant/java4ever-framework) library.

**Get quick help in our telegram
channel:** [![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/deplant\_chat)

### What you get

* Support of newest EVER-SDK
* No need to rebuild libs, just plug-in new SDKs
* Support of ABI 2.3
* Maven/Gradle support (lib deployed to Maven Central)
* Deploy any official Multisig Wallets easily
* Deploy & work with TIP3.1 contracts
* Work with EverNode SE Giver easily
* Replace with Multisig Givers with no test code change
* All inputs/outputs are auto-converted between Java & ABI

### Prerequisites

* **JDK 19** (19 or higher)
* **EVER-SDK** binary lib "**ton_client**" (build it yourself from github or
  get [precomiled ones](https://github.com/tonlabs/ever-sdk/blob/master/README.md#download-precompiled-binaries))
* **java.library.path** set. Add correct path to library as argument to Java
  run: `-Djava.library.path=<path_to_ton_client>`.

### Download

#### Gradle

```groovy
dependencies {
    implementation 'tech.deplant.java4ever:java4ever-binding:1.2.0'
    implementation 'tech.deplant.java4ever:java4ever-framework:1.2.0'
}
```

#### Maven

```xml

<dependency>
    <groupId>tech.deplant.java4ever</groupId>
    <artifactId>java4ever-binding</artifactId>
    <version>1.2.0</version>
</dependency>
<dependency>
<groupId>tech.deplant.java4ever</groupId>
<artifactId>java4ever-framework</artifactId>
<version>1.2.0</version>
</dependency>
```

### Can be used with any EVER-SDK version

You can use TON-SDK 1.37.0+ for your project or even load multiple libraries with different versions. You can use your
custom TON-SDK fork if you like. You can use even lower versions, but we will not fix any issues with it.