# Java4Ever

[![JDK version](https://img.shields.io/badge/Java-22-green.svg)](https://shields.io/)
[![SDK version](https://img.shields.io/badge/EVER%20SDK-v1.45.0-orange)](https://github.com/tonlabs/ever-sdk)
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
    * [Prerequisites for Kotlin](#prerequisites-for-kotlin)
    * [Add java4ever to your Maven/Gradle setup:](#add-java4ever-to-your-mavengradle-setup)
    * [Deploy your first contract](#deploy-your-first-contract)
  * [Examples and Guides](#examples-and-guides)
    * [Setting up SDK](#setting-up-sdk)
      * [Loading EVER-SDK library](#loading-ever-sdk-library)
      * [Creating config context and specifying endpoints](#creating-config-context-and-specifying-endpoints)
      * [Configuring SDK with Builder](#configuring-sdk-with-builder)
    * [Adding ABI, TVC & other artifacts](#adding-abi-tvc--other-artifacts)
    * [Crypto](#crypto)
      * [Creating a random keypair](#creating-a-random-keypair)
      * [Creating a random seed](#creating-a-random-seed)
      * [Deriving keys from seed](#deriving-keys-from-seed)
      * [Using existing keys & seeds](#using-existing-keys--seeds)
      * [Using Signing Box](#using-signing-box)
    * [Smart-contracts](#smart-contracts)
      * [Using already deployed contract](#using-already-deployed-contract)
      * [Accessing contract account metadata](#accessing-contract-account-metadata)
    * [Contract Generation](#contract-generation)
      * [Calling generator for your artifacts](#calling-generator-for-your-artifacts)
      * [Accessing your newly generated contract wrapper](#accessing-your-newly-generated-contract-wrapper)
      * [Accessing generated function wrappers](#accessing-generated-function-wrappers)
      * [Calling Functions in various ways](#calling-functions-in-various-ways)
      * [Deploying new contracts](#deploying-new-contracts)
    * [Using wallets and other standard contract wrappers](#using-wallets-and-other-standard-contract-wrappers)
      * [Sending Internal Message from Multisig Wallet](#sending-internal-message-from-multisig-wallet)
      * [Encoding as Payload](#encoding-as-payload)
    * [Deploying smart-contracts](#deploying-smart-contracts)
      * [Accessing Template](#accessing-template)
      * [Prepared deployment set](#prepared-deployment-set)
      * [Variations of running deploy](#variations-of-running-deploy)
      * [Switching Givers](#switching-givers)
      * [Offline deployment](#offline-deployment)
    * [Subsriptions](#subsriptions)
    * [Currency](#currency)
    * [Logging](#logging)
  * [Getting Help](#getting-help)
<!-- TOC -->

## Quick start

### Prerequisites

* Install **JDK 22** ([downloaded here](https://adoptium.net/temurin/releases/?version=22))
* Install **EverNode-SE** ([installation guide here](https://github.com/everx-labs/evernode-se))

### Prerequisites for Kotlin

* Install **Kotlin 2.0.0**
* Install **Gradle 8.8**
* Make sure to setup use of Gradle 8.8 and JDK 22 for compilation and runtime

**Note:** EverNode-SE needed only for quick start example, it's not a requirement

### Add java4ever to your Maven/Gradle setup:

* Gradle

```groovy
dependencies {
    implementation 'tech.deplant.java4ever:java4ever-framework:3.2.0'
}
```

* Maven

```xml

<dependency>
    <groupId>tech.deplant.java4ever</groupId>
    <artifactId>java4ever-framework</artifactId>
    <version>3.2.0</version>
</dependency>
```

### Deploy your first contract

```java
// initialize EVER-SDK library
EverSdk.load(); 
// create config context, save its id
int contextId = EverSdk.createWithEndpoint("http://localhost/graphql").orElseThrow();
// creates random pair of keys
var keys = Credentials.ofRandom(contextId); 
// use it to deploy a new contract
var contract = new SafeMultisigWalletTemplate()
        .prepareDeploy(contextId,0, keys, new BigInteger[]{keys.publicKeyBigInt()}, 1)
        .deployWithGiver(EverOSGiver.V2(contextId), EVER_ONE);
// get the contract info
System.out.println(contract.account().id() + " is active: " + contract.account().isActive());
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

### Adding ABI, TVC & other artifacts

**Java4Ever** includes easy API to work with files and java resources
(both json-based and byte[]-based). 
Here are simple examples of getting contract ABIs and TVCs artifacts:

* `ContractAbi.ofFile("/path/to/your.abi.json")` - reads abi from file (can be relative)
* `ContractAbi.ofResource("yourresource.abi.json")` - reads abi from resources of your project
* `ContractAbi.ofString("")` -reads abi from JSON string
* `ContractAbi.ofJsonNode(node)` - reads abi from Jackson framework's JsonNode object

* `Tvc.ofFile("/path/to/your.tvc")` - reads tvc from file (can be relative)
* `Tvc.ofResource("yourresource.tvc")` - reads tvc from resources of your project
* `Tvc.ofBase64String("")` -reads tvc from base64 encoded string
* `new Tvc(bytes)` - reads tvc from byte array

Also, you can check `JsonFile`, `JsonResource`, `ByteFile`, `ByteResource` helpers for custom artifacts.

### Crypto

**Java4Ever** includes basic helpers to
create your seeds, key pairs and signatures. If
you want some specific **EVER-SDK** functions, just use them
firectly as all **EVER-SDK** API is available from **Java4Ever**.

#### Creating a random keypair

```java
var keys = Credentials.ofRandom(contextId);
String sk = keys.secretKey();
String pk = keys.publicKey();
```

#### Creating a random seed

```java
var seed = Seed.ofRandom(contextId);
String wordsPhrase = seed.phrase();
int wordsCount = seed.words();
```

#### Deriving keys from seed

```java
var keys = Credentials.ofSeed(contextId,seed);
String sk = keys.secretKey();
String pk = keys.publicKey();
```

#### Using existing keys & seeds

```java
var seed = new Seed("your seed phrase with 12 words or 24 with second constructor param");
var keys = new Credentials("publickey_string","secretkey_string");
```

#### Using Signing Box

```java
int context = EverSdk.createDefault();
var keys = Env.RNG_KEYS();
// create implementation of your handle
var boxHandle = EverSdk.await(Crypto.registerSigningBox(context, new AppSigningBox() {
    @Override
    public String getPublicKey() {
        return "";
    }

    @Override
    public String sign(String unsigned) {
        return "";
    }
})).handle();
// use this handle when creating contract object
var contract = new EverWalletContract(context, 
                                      new Address("0:9400ec4b8629b5293bb6798bbcf3dd25d72e4f114226b5547777d0fc98fe53fa"), 
                                      new Abi.Signer.SigningBox(boxHandle));
// now contract calls will use your signing box
contract.sendTransaction(dest, value, bounce).call();
```

### Smart-contracts

#### Using already deployed contract

```java
// to use already deployed contract, you should know its ABI and its address
var deployedContractAbi = ContractAbi.ofResource("artifacts/giver/GiverV2.abi.json");
var deployedContractAddress = new Address("0:ece57bcc6c530283becbbd8a3b24d3c5987cdddc3c8b7b33be6e4a6312490415");
// if you don't know contract credentials, use Credentials.NONE
var deployedContractCredentials = new Credentials("2ada2e65ab8eeab09490e3521415f45b6e42df9c760a639bcf53957550b25a16",
                                                  "172af540e43a524763dd53b26a066d472a97c4de37d5498170564510608250c3");
// instantiate your contract
var giverContract = new AbstractContract(contextId,
                                         deployedContractAddress,
                                         deployedContractAbi,
                                         deployedContractCredentials);
// make a call by function name
var functionCallPrepare = giverContract.functionCallBuilder()
        .setFunctionName("getMessages")
        .setFunctionInputs(Map.of()) // provide a map of params
        .setReturnClass(Map.class)
        .build();
// if you didn't provide return class, use callAsMap() and getAsMap() to receive plain JSON
System.out.println(functionCallPrepare.getAsMap().toPrettyString());
```

If you don't want to write function calls by hand, 
use [Contract Generation](#contract-generation) tips below to generate your own Contract classes.

#### Accessing contract account metadata

To access account metadata of certain smart-contract, get Account object from any type of Contract
```java
Account acc = contract.account();
```
Alternatively, you can create Account object from any address
```java
Account acc = Account ofAddress(contextId, "0:ece57bcc6c530283becbbd8a3b24d3c5987cdddc3c8b7b33be6e4a6312490415");
```
Then, get all needed info from account
```java
acc.boc();
acc.code();
acc.codeHash();
acc.balance();
acc.accType();
```

### Contract Generation

#### Calling generator for your artifacts

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

If you're working with standard contracts, all wrappers are
already generated (for **Multisig Wallets**, **Givers**, **TIP3** and **TIP4**
contracts and so on -
check [javadoc](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-framework/latest/java4ever.framework/tech/deplant/java4ever/framework/contract/package-summary.html))

#### Accessing your newly generated contract wrapper

To access contract account, create instance of your contract class by
passing SDK Provider and address of deployed contract.

```java
MyContract contr = new MyContract(contextId, "0:your_contract_address");
```

#### Accessing generated function wrappers

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
* **...AsMap()** - each method have AsMap variant that returns Jackson framework's JsonNode object instead of static type result

#### Deploying new contracts

Second class created by contract generator is `MyContractTemplate.class`.
It's a companion class that stores ABI and TVC info for deployment.

### Using wallets and other standard contract wrappers

#### Sending Internal Message from Multisig Wallet

```java
var walletContract = new SafeMultisigWallet(contextId,"", walletKeys);
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

### Deploying smart-contracts

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

#### Prepared deployment set

`DeployHandle` is a handle of prepared deployment set with all needed params. 
As with function handles, `Template::prepareDeploy` params may vary depending on your contract -
your static variables and constructor params.

```java
DeployHandle deployHandle = myTemplate.prepareDeploy(contextId, Credentials.NONE,"hello_world");
```

#### Variations of running deploy

```java
// Deploys prepared contract deployment. Will work only when deploy target account address is already paid
// You can check target account address by using deployHandle.toAddress() call
MyContract myContract = deployHandle.deploy();
// deploys prepared contract deployment by using funds from specified wallet
MyContract myContract = deployHandle.deployWithGiver(walletContract, CurrencyUnit.VALUE(EVER,"1.25"));
// deploys prepared contract deployment by using funds from EverNode-SE giver
MyContract myContract = deployHandle.deployWithGiver(EverOSGiver.V2(contextId), CurrencyUnit.VALUE(EVER,"1.25"));
```

Each deployment returns a contract object after deploy is done. 
Also, you can use `deployHandle.toAddress()` if you need only address calculation.

#### Switching Givers

Deployment usually requires giving funds to target address of deployment. 
Here's the example of universal deployment that switches 
between **evernode-se** giver & **msig wallet** without any additional code:

```java
Giver giver = null;

if (isEverOsNet()) {
  giver = EverOSGiver.V2(contextId);
} else {
  giver = new SafeMultisigWallet(contextId,"0:your_address");
}

deployHandle.deployWithGiver(giver, CurrencyUnit.VALUE(EVER,"1.25"));
```

This is possible as all Java4Ever wallet classes are implementing Giver interface. 
You can also generate wrappers that implements Giver interface.


#### Offline deployment

Example of creation and signing offline messages for later sending. 

```java
int offlineContext = EverSdk.builder()
                     .networkSignatureId(1L)
                     .networkQueryTimeout(300_000L)
                     .build();
int i = 0;
var template = new EverWalletTemplate();
// let's generate 5 addresses that match certain condition and send them 1 ever
Predicate<Address> addressCondition = address -> address.makeAddrStd().contains("7777");
while (i < 5) {
    var seed = Env.RNG_SEED(); // creates new seed
    var keys = seed.deriveCredentials(offlineContext); // derives keys from seed
    // let's calculate future EVER Wallet address
    var stateInit = template.getStateInit(offlineContext, keys.publicKey(), BigInteger.ZERO);
    var address = template.getAddress(offlineContext, 0, stateInit);
    
    // check conditions if we want to deploy contract
    if (addressCondition.test(address)) {
        // let's create message bodies offline
        // here we send sendTransaction to ourselves
        var body = new EverWalletContract(offlineContext, address, keys).sendTransaction(address, EVER_ONE, false)
                                                                     .toPayload(false);

        System.out.printf("Address: %s%n", address);
        System.out.printf("Seed: %s, public: %s%n", seed.phrase(), keys.publicKey());
        System.out.printf("Message body: %s%n", body);
        System.out.printf("State Init: %s%n", stateInit);

        int onlineContext = EverSdk.createWithEndpoint("https://gql.venom.foundation/graphql");
        // let's send messages when we're online
        EverSdk.sendExternalMessage(onlineContext,
                                    address.makeAddrStd(),
                                    EverWalletTemplate.DEFAULT_ABI().ABI(),
                                    stateInit.cellBoc(),
                                    body.cellBoc(),
                                    null);
        i++;
    }
}
```

### Subsriptions

**Unstable** - Subscriptions were heavily changed in the last release, so there can be dragons.

Subscriptions consist of Subscription.Builder class where you describe your 
subscription details. After that, run the subscription with subscribe..() methods. 
Subscription supports sprcifying multiple GQL filters, multiple consumers, 
saving to queue, manual and auto-unsubscribing on condition.

```java
// let's specify what will consume our event:
Consumer<JsonNode> eventConsumer = jsonNode -> System.out.println(jsonNode.toPrettyString());
// describe our subscription in builder style
var subscriptionBuilder = Subscriptions
        .onAccounts("acc_type", "id")
        .addFilterOnSubscription("id: { eq: \"<your_address>\" }")
        .addFilterOnSubscription("code_hash: { eq: \"<your_hash>\" }")
        .addCallbackConsumer(eventConsumer)
        .setCallbackToQueue(true); // if you don't want to specify consumer, you can switch on adding to internal queue
// let's subsribe
var subscription1 = subscriptionBuilder.subscribeUntilCancel(1);
// let's unsubscribe
subscription1.unsubscribe();
// perhaps some messages were pu in the queue?
int size = subscription1.callbackQueue().size();
// let's reuse builder, but subscribe until first event is fired
var subscription2 = subscriptionBuilder.subscribeUntilFirst(1);
// another one, subscribed until certain condition
var subscription3 = subscriptionBuilder.subscribeUntilCondition(1, jsonNode -> !jsonNode.get("accounts").elements().hasNext());
```

### Currency

All solidity currency constants are available from CurrencyUnit class. 
You can retrieve final bigint about like this

```java
var everAmount = CurrencyUnit.VALUE(EVER, "2"); // 2_000_000_000 nanoevers
var everAmount = CurrencyUnit.VALUE(MILLIEVER, "500.3"); // 500_300_000 nanoevers
```

If your token has custom decimals count, you can specify it like this

```java
var tokenUnit = new CurrencyUnit.CustomToken(12); // my token has 12 decimals
// then use your own tokenunits in all your calls
var nanoValue = CurrencyUnit.VALUE(tokenUnit, "2.2"); // 2_200_000_000_000 nanotokens
```

### Logging

**Java4Ever** uses the JDK Platform Loggging (JEP 264: Platform Logging API and Service),
so can be easily bridged to any logging framework. For example, to use log4j2, just add `org.apache.logging.log4j:log4j-jpl` to your Maven/Gradle build.

## Getting Help

If you can't answer in this readme or have a bug/improvement to report:
* Ask in our [Telegram](https://t.me/deplant_chat_en) support chat
* Open a new [Issue](https://github.com/deplant/java4ever-framework/issues/new)
* Read Javadocs: [![javadoc](https://javadoc.io/badge2/tech.deplant.java4ever/java4ever-framework/javadoc.svg)](https://javadoc.io/doc/tech.deplant.java4ever/java4ever-framework)
