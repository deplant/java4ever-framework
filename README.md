# Java4Ever

[![JDK version](https://img.shields.io/badge/Java-19-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/EVER%20SDK-v1.43.2-orange)](https://github.com/tonlabs/ever-sdk)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)

* Discuss in
  Telegram: [![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/deplant\_chat\_en)
* Read full
  docs: [![javadoc](https://javadoc.io/badge2/tech.deplant.java4ever/java4ever-framework/javadoc.svg)](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-framework)

**Java4Ever** is a feature-rich framework for smart-contracts development, testing & accessing 
TVM-compatible blockchains like [Everscale](https://everscale.network/), [Venom](https://venom.network/), [GOSH](https://gosh.sh/) and so on. Closest alternative is a TypeScript-based [locklift](https://github.com/broxus/locklift) framework by Broxus.

Framework internally uses JSON-RPC connection to wrapped native EVER-SDK library ([java4ever-binding](https://github.com/deplant/java4ever-binding)).

## Features

* Auto-generation of Java classes for your contracts
* Auto-conversion of ABI types to Java
* Easy work with TIP 3.2 Fungible Tokens
* Easy work with Multisig Wallets
* Easy work with EverNode-SE Givers (with easy polymorph to giving from wallet on production)
* Access to transaction tree of a call
* Complete typed latest [EVER-SDK](https://github.com/tonlabs/ever-sdk/blob/master/docs/SUMMARY.md) API support
* Pluggable EVER-SDK library support (no rebuild needed, just plug-in SDK lib you like with special Loaders)

## Contents

<!-- TOC -->
* [Java4Ever](#java4ever)
  * [Features](#features)
  * [Contents](#contents)
  * [Quick start](#quick-start)
    * [Prerequisites](#prerequisites)
    * [Add java4ever to your Maven or Gradle setup:](#add-java4ever-to-your-maven-or-gradle-setup)
  * [Examples](#examples)
    * [Configuration](#configuration)
      * [Creating SDK Provider](#creating-sdk-provider)
    * [Reading ABI, TVC & other artifacts](#reading-abi-tvc--other-artifacts)
    * [Generating classes for your contracts](#generating-classes-for-your-contracts)
    * [Crypto](#crypto)
      * [Creating a random keypair](#creating-a-random-keypair)
      * [Creating a random seed](#creating-a-random-seed)
      * [Using existing keys & seeds](#using-existing-keys--seeds)
    * [Working with existing contracts](#working-with-existing-contracts)
      * [Accessing Contract](#accessing-contract)
      * [Accessing Function](#accessing-function)
      * [Calling Functions in various ways](#calling-functions-in-various-ways)
      * [Sending Internal Message from Multisig Wallet](#sending-internal-message-from-multisig-wallet)
      * [Encoding as Payload](#encoding-as-payload)
    * [Deploying new contracts](#deploying-new-contracts)
      * [Accessing Template](#accessing-template)
      * [Accessing Deployment Set](#accessing-deployment-set)
      * [Variations of deploy](#variations-of-deploy)
      * [Switching Givers](#switching-givers)
  * [Logging](#logging)
  * [Getting Help](#getting-help)
<!-- TOC -->

## Quick start

### Prerequisites

* Install **JDK 19** or higher ([link](https://adoptium.net/temurin/releases?version=19))
* Build **EVER-SDK** binary lib "**ton_client**"(.so/.dll) (or
  get [precomiled one](https://github.com/tonlabs/ever-sdk/blob/master/README.md#download-precompiled-binaries))

### Add java4ever to your Maven or Gradle setup:

* Gradle

```groovy
dependencies {
    implementation 'tech.deplant.java4ever:java4ever-framework:1.8.0'
}
```

* Maven

```xml

<dependency>
    <groupId>tech.deplant.java4ever</groupId>
    <artifactId>java4ever-framework</artifactId>
    <version>1.8.0</version>
</dependency>
```

## Examples

### Configuration

#### Creating SDK Provider

You can find a list of endpoints here: https://docs.evercloud.dev/products/evercloud/networks-endpoints

If you're working with Everscale mainnet, here you can register your app and receive "ProjectID" part of the URL: https://dashboard.evercloud.dev/

```java
var sdk = Sdk.builder().networkEndpoints("localhost")
			              .build(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
```
You can add "ton_client" lib by multiple ways.
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
* `new Tvc(bytes)` - reads tvc from byte array

Also, you can check JsonFile, JsonResource, ByteFile, ByteResource helpers for custom artifacts.

### Generating classes for your contracts

Contract Wrapper Generator will create java wrapper classes for all your contracts. You need only `abi.json` and `.tvc` artifacts of your contracts. 

Run the following, specifying your artifacts and where to place generated classes:

```java
ContractWrapper.generate(ContractAbi.ofResource("mycontract.abi.json").abiContract(),
                         Tvc.ofResource("mycontract.tvc"),
                         Path.of("src/gen/java"),
                         "MyContract",
                         "org.example.contract",
                         "org.example.template",
                         new String[]{});
```

Contract and template wrappers will appear in packages that you specified.

### Crypto

#### Creating a random keypair

```java
var keys = Credentials.RANDOM(sdk);
String sk = keys.secretKey();
String pk = keys.publicKey();
```

#### Creating a random seed

```java
var seed = Seed.RANDOM(sdk);
String wordsPhrase = seed.phrase();
int wordsCount = seed.words();

var keys = Credentials.ofSeed(sdk,seed);
String sk = keys.secretKey();
String pk = keys.publicKey();
```

#### Using existing keys & seeds

```java
var seed = new Seed("your seed phrase with 12 words or 24 with second constructor param");
var keys = new Credentials("publickey_string","secretkey_string");
```

### Working with existing contracts

If you generated MyContract via [generator](#contract-wrappers-generation), all its methods are now available from MyContract.class.
If you're working with standard contracts, all wrappers are already generated (for multisig wallets, givers, TIP3 and TIP4 contracts and so on - check [javadoc](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-framework/latest/java4ever.framework/tech/deplant/java4ever/framework/contract/package-summary.html))

#### Accessing Contract

```java
MyContract contr = new MyContract(sdk, "0:your_contract_address");

contr.accountBalance(); // currency balance on account
contr.account().isActive(); // contract status
```

or with credentials for signing external calls:

```java
MyContract contr = new MyContract(sdk, "0:your_contract_address", keys);
```

#### Accessing Function

```java
FunctionHandle getCustodiansFunctionHandle = contr.getCustodians();
```

Function in this example doesn't have params, but yours can have.

#### Calling Functions in various ways

```java
MyContract.ResultOfGetCustodians custodians = getCustodiansFunctionHandle.get();

Map<String,Object> custodiansMap = getCustodiansFunctionHandle.getAsMap();

MyContract.ResultOfGetCustodians custodians = getCustodiansFunctionHandle.call();

Map<String,Object> custodiansMap = getCustodiansFunctionHandle.callAsMap();

MyContract.ResultOfGetCustodians custodians = getCustodiansFunctionHandle.getLocal(locallySavedBoc);
```

All the described functions, handles and return types are auto-generated when you generate contract wrapper

**Variants of calls to function**:

* **get()** - runs getter method and returns auto-generated type
* **call()** - makes external call (make sure you added credentials to contract object if contract checks signatures)
* **getLocal()** - runs getter against provided boc
* **...AsMap()** - each method have AsMap variant that returns Map<String, Object> insted of type

#### Sending Internal Message from Multisig Wallet

```java
var walletContract = new SafeMultisigWallet(sdk,"", walletKeys);
getCustodiansFunctionHandle.sendFrom(walletContract, CurrencyUnit.VALUE(EVER,"1.25"), true, MessageFlag.FEE_EXTRA);
```
**sendFrom()** method also has **sendFromAsMap()** variant.

Calls and sends also has **...Tree()** variants that can be used to monitor transaction tree execution and collect errors.


#### Encoding as Payload

```java
var payload = getCustodiansFunctionHandle.toPayload();
```

### Deploying new contracts

#### Accessing Template

```java
MyContractTemplate myTemplate = new MyContractTemplate();

var abi = myTemplate.abi(); // getting ABI from template
var tvc = myTemplate.tvc(); // getting TVC from template

myTemplate.tvc().code() // getting code cell
myTemplate.tvc().codeHash() // getting code hash
```

There are much more methods for TVC and ABI, including decoding and encoding 
initial data, various helpers for all sort of interactions.

#### Accessing Deployment Set

```java
DeployHandle deployHandle = myTemplate.prepareDeploy(sdk, Credentials.NONE,"hello_world");
```

As with FunctionHandle, prepareDeploy() of your contract DeployHandle can have additional params - your static variables and constructor params.

#### Variations of deploy

```java
MyContract myContract = deployHandle.deploy();

MyContract myContract = deployHandle.deployWithGiver(walletContract, CurrencyUnit.VALUE(EVER,"1.25"));

MyContract myContract = deployHandle.deployWithGiver(EverOSGiver.V2(sdk), CurrencyUnit.VALUE(EVER,"1.25"));
```

Each deployment creates a ready contract object after deploy is done. 
Also, you can use `deployHandle.toAddress()` if you need only address calculation.

#### Switching Givers

Here's the example of universal deployment that switches 
between **evernode-se** giver & **msig wallet** without any additional code:

```java
Giver giver = null;

if (isEverOsNet()) {
  giver = EverOSGiver.V2(sdk);
} else {
  giver = new SafeMultisigWallet(sdk,"0:your_address");
}

deployHandle.deployWithGiver(giver, CurrencyUnit.VALUE(EVER,"1.25"));
```

This is possible as all Java4Ever wallet classes are implementing Giver interface.

## Logging

java4ever-framework uses the JDK Platform Loggging (JEP 264: Platform Logging API and Service),
so can be easily bridged to any logging framework. For example, to use log4j2, just add *org.apache.logging.log4j:log4j-jpl* to your Maven/Gradle build.

## Getting Help

If you can't answer in this readme or have a bug/improvement to report:
* Ask in our [Telegram](https://t.me/deplant_chat_en) support chat
* Open a new [Issue](https://github.com/deplant/java4ever-framework/issues/new)