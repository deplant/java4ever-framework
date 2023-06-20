# java4ever-framework

[![JDK version](https://img.shields.io/badge/Java-19-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/EVER%20SDK-v1.43.2-orange)](https://github.com/tonlabs/ever-sdk)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)

* Discuss in
  Telegram: [![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/deplant\_chat\_en)
* Read full
  docs: [![javadoc](https://javadoc.io/badge2/tech.deplant.java4ever/java4ever-framework/javadoc.svg)](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-framework)

**java4ever** is a Java 19 suite for smart-contracts development, testing & accessing DApps for
[Everscale](https://everscale.network/) blockchain network much like [web3j](https://github.com/web3j/web3j) for
Ethereum and close to Truffle, Hardhat & [locklift](https://github.com/broxus/locklift).

With **java4ever** you can connect your enterprise services on **Java** to
most scalable smart-contract network. It is also suitable for any Everscale forks like GOSH or Venom.

With java4ever you get:

* Complete implementation of
  Everscale [EVER-SDK JSON-RPC API](https://github.com/tonlabs/ever-sdk/blob/master/docs/SUMMARY.md)
* All main Multisig Wallets variations support (easy deploy & calls)
* Pluggable EVER-SDK client library support (no rebuild needed, just plug-in SDK lib you like)
* Maven/Gradle support (deployed
  to [Maven Central](https://mvnrepository.com/artifact/tech.deplant.java4ever/java4ever-framework))
* TIP 3.2 Fungible Tokens helpers (easy deploy & calls)
* EverNodeSE Giver helpers (and you can polymorph to Multisig Givers with no code change or implement custom)
* Powerful auto-conversion between Java types & ABI expectations
* Transaction tree of a complex call can be accessed

**java4ever** uses blocking code (that is now
a [best practice](https://github.com/alexcheng1982/jdk-loom-faq#structured-concurrency)
due to Loom [Structured Concurrency](https://openjdk.org/jeps/428)).
Framework internally uses JSON-RPC connection to native EVER-SDK library binded to
Java ([java4ever-binding](https://github.com/deplant/java4ever-binding)).

## Quick start

#### Prerequisites

* Install **JDK 19** or higher ([link](https://adoptium.net/temurin/releases?version=19))
* Build **EVER-SDK** binary lib "**ton_client**"(.so/.dll) (or
  get [precomiled one](https://github.com/tonlabs/ever-sdk/blob/master/README.md#download-precompiled-binaries))

#### Add java4ever to your Maven of Gradle setup:

* Gradle

```groovy
dependencies {
    implementation 'tech.deplant.java4ever:java4ever-framework:1.7.0'
}
```

* Maven

```xml

<dependency>
    <groupId>tech.deplant.java4ever</groupId>
    <artifactId>java4ever-framework</artifactId>
    <version>1.7.0</version>
</dependency>
```

#### Add custom EVER-SDK library to your code

You can add "ton_client" lib by multiple ways:

* Specify absolute path to your library in code:

```java
var sdk1 = new SdkBuilder().create(new AbsolutePathLoader("c:/opt/sdk/ton_client.dll"));
```

* Specify absolute path in system env variables:

```java
var sdk2 = new SdkBuilder().create(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB_PATH"));
```

* Place it in build current dir and specify filename in code:

```java
var sdk3 = new SdkBuilder().create(AbsolutePathLoader.ofUserDir("ton_client.so"));
```

* Use `-Djava.library.path` parameter to VM and then specify only lib name:

```java
var sdk4 = new SdkBuilder().create(new JavaLibraryPathLoader("ton_client"));
```

## Examples

### Configuration

#### Create SDK Provider

You can find a list of endpoints here: https://docs.evercloud.dev/products/evercloud/networks-endpoints

If you're working with Everscale mainnet, here you can register your app and receive "ProjectID" part of the URL: https://dashboard.evercloud.dev/

```java
var sdkDev = Sdk.builder().networkEndpoints("localhost")
			              .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
```

Variants of loading ton_client lib:
* `AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB")` - path from Environment variable
* `AbsolutePathLoader.ofUserDir("libton_client.so")` - file from ~ (user home)
* `new AbsolutePathLoader(Path.of("\home\ton\lib\libton_client.so"))` - any absolute path
* `new JavaLibraryPathLoader("ton_client");` - gets library from java.library.path JVM argument

### Reading ABI, TVC & other artifacts

* `ContractAbi.ofFile("/path/to/your.abi.json")` - reads abi from file (can be relative)
* `ContractAbi.ofResource("yourresource.abi.json")` - reads abi from resources of your project
* `ContractAbi.ofString("")` -reads abi from JSON string
* `ContractAbi.ofJsonNode(node)` - reads abi from JSON node

* `Tvc.ofFile("/path/to/your.tvc")` - reads tvc from file (can be relative)
* `Tvc.ofResource("yourresource.tvc")` - reads tvc from resources of your project
* `Tvc.ofBase64String("")` -reads tvc from base64 encoded string
* `new Tvc(bytes)` - reads tvc from JSON node

Also, you can check JsonFile, JsonResource, ByteFile, ByteResource helpers for custom artifacts.

### Contract Wrappers Generation

```java
ContractWrapper.generate(ContractAbi.ofResource("mycontract.abi.json").abiContract(),
                         Tvc.ofResource("mycontract.tvc"),
                         Path.of("src/gen/java"),
                         "MyContract",
                         "org.example.contract",
                         "org.example.template",
                         new String[]{});
```

### Crypto

#### Create a random keypair

```java
var keys = Credentials.RANDOM(sdk);
String sk = keys.secretKey();
String pk = keys.publicKey();
```

#### Create a random seed and keys from it

```java
var seed = Seed.RANDOM(sdk);
String wordsPhrase = seed.phrase();
int wordsCount = seed.words();

var keys = Credentials.ofSeed(sdk,seed);
String sk = keys.secretKey();
String pk = keys.publicKey();
```

### Contracts

If you generated MyContract previously, all its methods are now available from MyContract.class.

#### Create Contract Object

```java
MyContract contr = new MyContract(SDK, "0:your_contract_address");
```

or with credentials for external calls:

```java
MyContract contr = new MyContract(SDK, "0:your_contract_address", keys);
```

#### Accessing Functions

```java
FunctionHandle getCustodiansFunctionHandle = contr.getCustodians();
```

Function in this example doesn't have params, but yours can have.

#### Now you can call it as you like

```java
MyContract.ResultOfGetCustodians custodians = getCustodiansFunctionHandle.get();

Map<String,Object> custodiansMap = getCustodiansFunctionHandle.getAsMap();

MyContract.ResultOfGetCustodians custodians = getCustodiansFunctionHandle.call();

Map<String,Object> custodiansMap = getCustodiansFunctionHandle.callAsMap();

MyContract.ResultOfGetCustodians custodians = getCustodiansFunctionHandle.getLocal(locallySavedBoc);
```

All the described functions, handles and return types are auto-generated when you generate contract wrapper

Variants of calls to function:

* **get()** - runs getter method and returns auto-generated type
* **call()** - makes external call (make sure you added credentials to contract object if contract checks signatures)
* **getLocal()** - runs getter against provided boc
* **...AsMap()** - each method have AsMap variant that returns Map<String, Object> insted of type

#### Send internal message with Msig

```java
var walletContract = new SafeMultisigWallet(sdk,"", walletKeys);
getCustodiansFunctionHandle.sendFrom(walletContract, CurrencyUnit.VALUE(EVER,"1.25"), true, MessageFlag.FEE_EXTRA);
```

**sendFrom()** method also has **sendFromAsMap()** variant.

Calls and sends also has **...Tree()** variants that can be used to monitor transaction tree execution and collect errors.

### Deployment

#### Create ContractTemplate Object

```java
MyContractTemplate myTemplate = new MyContractTemplate();
```

#### Accessing deploy initial data

```java
DeployHandle deployHandle = myTemplate.prepareDeploy(sdk, Credentials.NONE,"hello_world");
```

As with FunctionHandle, prepareDeploy() of your contract DeployHandle can have additional params - your static variables and constructor params.

#### Deployment Variations

```java
MyContract myContract = deployHandle.deploy();

MyContract myContract = deployHandle.deployWithGiver(walletContract, CurrencyUnit.VALUE(EVER,"1.25"));

MyContract myContract = deployHandle.deployWithGiver(EverOSGiver.V2(sdk), CurrencyUnit.VALUE(EVER,"1.25"));
```

Each deployment creates a ready contract object after deploy is done. 
Also, you can use `deployHandle.toAddress()` if you need only address calculation.

#### Switch giver

Here's the example of universal deployment that switches 
between Local Node giver & real wallet without any additional code:

```java
Giver giver = null;

if(isEverOsNet()){
  giver = EverOSGiver.V2(sdk);
}else{
  giver = walletContract;
}

deployHandle.deployWithGiver(giver, CurrencyUnit.VALUE(EVER,"1.25"));
```

This is possible as standard wallet contracts are implementing Giver interface.

## Logging

java4ever-framework uses the JDK Platform Loggging (JEP 264: Platform Logging API and Service),
so can be easily bridged to any logging framework. For example, to use log4j2, just add *org.apache.logging.log4j:log4j-jpl* to your Maven/Gradle build.

## Getting Help

If you can't answer in this readme or have a bug/improvement to report:
* Ask in our [Telegram](https://t.me/deplant_chat_en) support chat
* Open a new [Issue](https://github.com/deplant/java4ever-framework/issues/new)