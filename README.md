# Java4Ever

[![JDK version](https://img.shields.io/badge/Java-20-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/EVER%20SDK-v1.44.3-orange)](https://github.com/tonlabs/ever-sdk)
[![License](https://img.shields.io/badge/License-Apache%202.0-brown.svg)](https://shields.io/)
[![Channel on Telegram](https://img.shields.io/badge/chat-on%20telegram-9cf.svg)](https://t.me/deplant\_chat\_en)
[![javadoc](https://javadoc.io/badge2/tech.deplant.java4ever/java4ever-framework/javadoc.svg)](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-framework)

<p align="center">
  <a href="https://github.com/venom-blockchain/developer-program">
    <img src="https://raw.githubusercontent.com/venom-blockchain/developer-program/main/vf-dev-program.png" alt="Logo" width="366.8" height="146.4">
  </a>
</p>



**Java4Ever** is a feature-rich framework for smart-contracts development, testing & accessing 
TVM-compatible blockchains like [Everscale](https://everscale.network/), [Venom](https://venom.network/), [GOSH](https://gosh.sh/) and so on. Closest alternative is a TypeScript-based [locklift](https://github.com/broxus/locklift) framework by Broxus.

Framework internally uses JSON-RPC connection to wrapped native EVER-SDK library ([java4ever-binding](https://github.com/deplant/java4ever-binding)).

## Features

* Auto-generation of Java smart contract wrappers to create, deploy, transact with and call smart contracts from native Java code
* Auto-conversion of ABI types to Java native types for any input/output
* Easy work with TIP 3.2 Fungible Tokens
* Easy work with Multisig Wallets
* Easy work with EverNode-SE Givers (with easy polymorph to giving from wallet on production)
* Access to transaction tree of complex calls
* Complete typed implementation of latest [EVER-SDK](https://github.com/tonlabs/ever-sdk/blob/master/docs/SUMMARY.md) JSON-RPC client API
* Pluggable EVER-SDK library support (no rebuild needed, just plug-in SDK lib you like with special Loaders)

Java4Ever only runtime dependencies are its own binding and utils libs and Jackson Core for fast JSON serialisation/deserialization It also uses JavaPoet for generating smart contract wrappers.

## Contents

<!-- TOC -->
* [Java4Ever](#java4ever)
  * [Features](#features)
  * [Contents](#contents)
  * [Quick start](#quick-start)
    * [Prerequisites](#prerequisites)
    * [Add java4ever to your Maven or Gradle setup:](#add-java4ever-to-your-maven-or-gradle-setup)
  * [Examples](#examples)
    * [SDK Provider](#sdk-provider)
      * [Creating SDK](#creating-sdk)
      * [Configuring SDK with Builder](#configuring-sdk-with-builder)
      * [Custom EVER-SDK Library loading variants](#custom-ever-sdk-library-loading-variants)
    * [ABI, TVC & other artifacts](#abi-tvc--other-artifacts)
    * [Contract Generation](#contract-generation)
    * [Crypto](#crypto)
      * [Creating a random keypair](#creating-a-random-keypair)
      * [Creating a random seed](#creating-a-random-seed)
      * [Using existing keys & seeds](#using-existing-keys--seeds)
    * [Working with deployed contracts](#working-with-deployed-contracts)
      * [Accessing Contract](#accessing-contract)
      * [Accessing Function](#accessing-function)
      * [Calling Functions in various ways](#calling-functions-in-various-ways)
      * [Sending Internal Message from Multisig Wallet](#sending-internal-message-from-multisig-wallet)
      * [Encoding as Payload](#encoding-as-payload)
    * [Deploying new contracts](#deploying-new-contracts)
      * [Accessing Template](#accessing-template)
      * [Accessing Deployment Set](#accessing-deployment-set)
      * [Variations of running deploy](#variations-of-running-deploy)
      * [Switching Givers](#switching-givers)
    * [Logging](#logging)
  * [Getting Help](#getting-help)
<!-- TOC -->

## Quick start

### Prerequisites

* Install **JDK 20** ([link](https://adoptium.net/temurin/releases?version=20))

### Add java4ever to your Maven or Gradle setup:

* Gradle

```groovy
dependencies {
    implementation 'tech.deplant.java4ever:java4ever-framework:2.5.0'
}
```

* Maven

```xml

<dependency>
    <groupId>tech.deplant.java4ever</groupId>
    <artifactId>java4ever-framework</artifactId>
    <version>2.5.0</version>
</dependency>
```

## Examples

### SDK Provider

**Sdk** class is a provider of connection to EVER-SDK lib and TVM blockchain.
It is a primary object that you need to run most interactions in **Java4Ever**:

#### Creating SDK

```java
var sdk1 = Sdk.DEFAULT();
var sdk2 = Sdk.DEFAULT("http://localhost/graphql");
var sdk3 = Sdk.builder().networkEndpoints("http://localhost/graphql").build();
```
You can find a list of endpoints here: https://docs.evercloud.dev/products/evercloud/networks-endpoints

If you're working with Everscale mainnet, here you can register your app and receive "ProjectID" part of the URL: https://dashboard.evercloud.dev/

#### Configuring SDK with Builder

**Sdk.Builder** is a Builder-style config for **EVER-SDK**, so you can easily config only needed parts of library.
```java
var sdk = Sdk.builder()
             .networkEndpoints("http://localhost/graphql")
             .abiWorkchain(-1)
             .networkRetriesCount(10)
             .abiMessageExpirationTimeout(30000)
             .build();
```

If you want to use custom `ton-client` lib or have some problem with the included ones, specify custom location as:

```java
var sdk = Sdk.builder()
             .networkEndpoints("http://localhost/graphql")
             .build(new AbsolutePathLoader(Path.of("\home\ton\lib\libton_client.so")));
```
You can find [precompiled ton_client files](https://github.com/tonlabs/ever-sdk/blob/master/README.md#download-precompiled-binaries) here. 
Specifying path to downloaded custom "ton_client" libs can be done 
in multiple ways by using different loaders.

#### Custom EVER-SDK Library loading variants

* `AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB")` - path from Environment variable
* `AbsolutePathLoader.ofUserDir("libton_client.so")` - file from ~ (user home)
* `new AbsolutePathLoader(Path.of("\home\ton\lib\libton_client.so"))` - any absolute path
* `new JavaLibraryPathLoader("ton_client");` - gets library from java.library.path JVM argument

### ABI, TVC & other artifacts

**Java4Ever** includes easy API to work with files and java resources
(both json-based and byte[]-based). 
Here are simple examples of getting contract ABIs and TVCs artifacts:

* `ContractAbi.ofFile("/path/to/your.abi.json")` - reads abi from file (can be relative)
* `ContractAbi.ofResource("yourresource.abi.json")` - reads abi from resources of your project
* `ContractAbi.ofString("")` -reads abi from JSON string
* `ContractAbi.ofJsonNode(node)` - reads abi from JSON node

* `Tvc.ofFile("/path/to/your.tvc")` - reads tvc from file (can be relative)
* `Tvc.ofResource("yourresource.tvc")` - reads tvc from resources of your project
* `Tvc.ofBase64String("")` -reads tvc from base64 encoded string
* `new Tvc(bytes)` - reads tvc from byte array

Also, you can check `JsonFile`, `JsonResource`, `ByteFile`, `ByteResource` helpers for custom artifacts.

### Contract Generation

`ContractWrapper` class is a generator that will create java wrapper 
classes for all your contracts. You need only `abi.json` and `.tvc` 
artifacts of your contracts as a source for code generation. 

Run the following, specifying your artifacts and where to place 
generated classes in params:

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

**Java4Ever** includes basic helpers to 
create your seeds, key pairs and signatures. If 
you want some specific **EVER-SDK** functions, just use them 
firectly as all **EVER-SDK** API is available from **Java4Ever**.

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

### Working with deployed contracts

If you generated your contract wrappers via [generator](#generating-classes-for-your-contracts) 
(`MyContract` in this example), 
all its methods are now available from `MyContract.class`.
If you're working with standard contracts, all wrappers are 
already generated (for **Multisig Wallets**, **Givers**, **TIP3** and **TIP4** 
contracts and so on - 
check [javadoc](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-framework/latest/java4ever.framework/tech/deplant/java4ever/framework/contract/package-summary.html))

#### Accessing Contract

To access contract account, create instance of your contract class by 
passing SDK Provider and address of deployed contract.

```java
MyContract contr = new MyContract(sdk, "0:your_contract_address");

contr.accountBalance(); // currency balance on account
contr.account().isActive(); // contract status
```

or with additional `Credentials` param for signing external calls:

```java
MyContract contr = new MyContract(sdk, "0:your_contract_address", keys);
```

#### Accessing Function

Now, when your contract object is created, just get handle of one 
of functions by calling one of the contract methods.

```java
FunctionHandle getCustodiansFunctionHandle = contr.getCustodians();
```

Function in this example doesn't have params, but yours can have.

#### Calling Functions in various ways

With `FunctionHandle` you can make external calls, run get methods using remote or local boc and so on like this:

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

Calls and sends also has **...Tree()** variants that can 
be used to monitor transaction tree execution and collect errors.

#### Encoding as Payload

You can encode `FunctionHandle` as a payload for internal call like this:

```java
var payload = getCustodiansFunctionHandle.toPayload();
```

### Deploying new contracts

Second class created by contract generator is `MyContractTemplate.class`. 
It's a companion class that stores ABI and TVC info for deployment.

#### Accessing Template

You can create template object with no additional params. 
If you didn't use generator, use `AbstractTemplate` class and 
pass ABI and TVC to it manually.

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

`DeployHandle` is a handle of prepared deployment with all needed params. 
As with function handles, `Template::prepareDeploy` params may vary depending on your contract -
your static variables and constructor params.

```java
DeployHandle deployHandle = myTemplate.prepareDeploy(sdk, Credentials.NONE,"hello_world");
```

#### Variations of running deploy

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

### Logging

**Java4Ever** uses the JDK Platform Loggging (JEP 264: Platform Logging API and Service),
so can be easily bridged to any logging framework. For example, to use log4j2, just add `org.apache.logging.log4j:log4j-jpl` to your Maven/Gradle build.

## Getting Help

If you can't answer in this readme or have a bug/improvement to report:
* Ask in our [Telegram](https://t.me/deplant_chat_en) support chat
* Open a new [Issue](https://github.com/deplant/java4ever-framework/issues/new)
* Read Javadocs: [![javadoc](https://javadoc.io/badge2/tech.deplant.java4ever/java4ever-framework/javadoc.svg)](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-framework)
