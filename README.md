# java4ever:framework

[![JDK version](https://img.shields.io/badge/Java-17+-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/TON%20SDK-v1.28.1-orange)](https://github.com/tonlabs/TON-SDK/tree/1.28.0)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)

Java Framework for testing and integration with Everscale network.

Java framework & binding library over Everscale TON-SDK. Native interconnection is not based on JNI derivatives but on
modern [Foreign Memory Access API](https://openjdk.java.net/jeps/393)

**Get quick help in our telegram
channel:** [![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/ton\_sdk)

### Goals

* Provide Java binding for TON-SDK based on modern Java native memory access
* Provide Java framework for Everscale development and testing
* Support any modern versions of TON-SDK, including your custom ones without rebuild
* Support different ways to plug TON-SDK library
* Support arbitrarily complex objects (with deep inheritance hierarchies and extensive use of generic types)

### Download (Framework & Binding)

#### Gradle

```gradle
dependencies {
  implementation 'tech.deplant.binding:binding:1.1.5'
  implementation 'tech.deplant.binding:framework:1.1.5'  
}
```

#### Maven

```xml

<dependency>
    <groupId>tech.deplant.binding</groupId>
    <artifactId>binding</artifactId>
    <version>1.1.5</version>
</dependency>
<dependency>
<groupId>tech.deplant.binding</groupId>
<artifactId>framework</artifactId>
<version>1.1.5</version>
</dependency>
```

### Download (Binding-only)

#### Gradle

```gradle
dependencies {
  implementation 'tech.deplant.binding:binding:1.1.5'
}
```

#### Maven

```xml

<dependency>
    <groupId>tech.deplant.binding</groupId>
    <artifactId>binding</artifactId>
    <version>1.1.5</version>
</dependency>
```

## Features

### Can be used with any TON-SDK version

You can use TON-SDK 1.16.0+ for your project or even load multiple libraries with different versions. You can use your
custom TON-SDK fork if you like. 