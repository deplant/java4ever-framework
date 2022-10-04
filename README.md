# java4ever-framework

[![javadoc](https://javadoc.io/badge2/tech.deplant.java4ever/java4ever-framework/javadoc.svg)](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-framework)
[![JDK version](https://img.shields.io/badge/Java-19+-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/TON%20SDK-v1.37+-orange)](https://github.com/tonlabs/TON-SDK/tree/1.28.0)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)

**Get quick help in our telegram
channel:** [![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/deplant\_chat)

**java4ever:framework** is a Java 19 framework for development, testing & DApps for
[Everscale](https://everscale.network/) network.
Framework is based (and dependant) on [java4ever-binding](https://github.com/deplant/java4ever-framework) library. With
java4ever you get:

* Newest EVER-SDKs/ABI 2.3 support (without rebuild, just plug-in SDK lib you like)
* Maven/Gradle support (deployed to Maven Central)
* Multisig Wallets helpers (easy deploy & transactions)
* TIP 3.1 helpers (easy deploy, transactions & getters)
* EverNode SE Giver helpers (you can switch to Multisig Givers with no code change or implement custom)
* Auto-conversion of types between Java & ABI
* Transaction tree of calls can be viewed

## Quick start

#### Prerequisites

* Install **JDK 19** or higher ([link](https://adoptium.net/temurin/releases?version=19))
* Build **EVER-SDK** binary lib "**ton_client**"(.so/.dll) (or
  get [precomiled one](https://github.com/tonlabs/ever-sdk/blob/master/README.md#download-precompiled-binaries))

#### Add java4ever to your Maven of Gradle setup:

* Gradle

```groovy
dependencies {
    implementation 'tech.deplant.java4ever:java4ever-binding:1.2.0'
    implementation 'tech.deplant.java4ever:java4ever-framework:1.2.0'
}
```

* Maven

```xml

<dependencies>
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
</dependencies>
```

#### Add Sdk lib to your code

You can add "ton_client" lib a multiply ways:

* Specify absolute path to your library in code:

```jshelllanguage
var sdk1 = new SdkBuilder().create(new AbsolutePathLoader("c:/opt/sdk/ton_client.dll"));
```

* Specify absolute path in system env variables:

```jshelllanguage
var sdk2 = new SdkBuilder().create(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB_PATH"));
```

* Place it in build current dir and specify filename in code:

```jshelllanguage
var sdk3 = new SdkBuilder().create(AbsolutePathLoader.ofUserDir("ton_client.so"));
```

* Use `-Djava.library.path` parameter to VM and then specify only lib name:

```jshelllanguage
var sdk4 = new SdkBuilder().create(new JavaLibraryPathLoader("ton_client"));
```

## Examples

### Configuration

#### Specify endpoints

```jshelllanguage
var sdkDev = new SdkBuilder()
		.networkEndpoints("https://eri01.main.everos.dev", "https://gra01.main.everos.dev")
		.create(JavaLibraryPathLoader.TON_CLIENT);
```

### Crypto

#### Create a random keypair

```jshelllanguage
{
	var keys = Credentials.RANDOM(sdk);
	String sk = keys.secretKey();
	String pk = keys.publicKey();
}
```

#### Create a random seed

```jshelllanguage
{
	var seed = Seed.RANDOM(SDK);
	String wordsPhrase = seed.phrase();
	int wordsCount = seed.words();
}
```