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

### Contracts

#### Describe a contract

```jshelllanguage
{
	var keysOfContract = new Credentials("1fae0df1eee24bc61fbb9230bdac07503b77ceac7700651bec8250df97b6f94f",
	                                     "8b55abc280dd0b741d78961b0f8f4d8d235f30f122bc5829b6e598e71331c01c");
	OwnedContract myContract = new OwnedContract(sdk,
	                                             new Address(
			                                             "0:273642fe57e282432bda8f16c69ea19a94b13db05986e11585a5121bcfec3fe0"),
	                                             ContractAbi.ofFile("~/MyContract.abi.json"),
	                                             keysOfContract);
}
```

#### Run getter

```jshelllanguage
{
	Map<String, Object> functionInputs = Map.of();
	Abi.FunctionHeader header = null;
	myContract.runGetter("getOwner", functionInputs, header).get("value0");
}
```

#### Call a contract

```jshelllanguage
{
	Map<String, Object> functionInputs = Map.of();
	Abi.FunctionHeader header = null;
	myContract.callExternal("getOwner", functionInputs, header).get("value0");
}
```

#### Send internal message with Msig

```jshelllanguage
{
	Map<String, Object> functionInputs = Map.of();
	Abi.FunctionHeader header = null;
	String payload = myContract.encodeInternalPayload("publishCustomTask", functionInputs, header);
	var addressOfMsig = new Address("0:273642fe57e282432bda8f16c69ea19a94b13db05986e11585a5121bcfec3fe0");
	var keysOfMsig = new Credentials("1fae0df1eee24bc61fbb9230bdac07503b77ceac7700651bec8250df97b6f94f",
	                                 "8b55abc280dd0b741d78961b0f8f4d8d235f30f122bc5829b6e598e71331c01c");
	BigInteger sendValue = EVER.amount();
	Msig msig = new Msig(sdk, addressOfMsig, keysOfMsig);
	msig.send(myContract.address(), sendValue, true, 0, payload); // sends internal message with payload
}
```

### Templates

#### Describe a template

```jshelllanguage
{
	ContractTemplate template = new ContractTemplate(ContractAbi.ofFile("~/MyContract.abi.json"),
	                                                 ContractTvc.ofFile("~/MyContract.tvc"));
	MsigTemplate safeTemplate = MsigTemplate.SAFE(); // msig templates are included
}
```

#### Deploy template

```jshelllanguage
{
	Credentials keys = Credentials.RANDOM(sdk);
	Map<String, Object> initialData = Map.of("initDataParam1", "helloWorld!"); // one static initData var
	Map<String, Object> constructorInputs = Map.of(); // no inputs
	OwnedContract contract = template.deploy(sdk, 0, initialData, keys, constructorInputs);
}
```

#### Check ABI

```jshelllanguage
{
	ContractAbi abi1 = template.abi();
	ContractAbi abi2 = ContractAbi.ofFile("~/MyContract.abi.json");
	boolean hasSend = abi1.hasFunction("sendTransaction");
	String abiType = abi2.functionOutputType("getBalance", "value0").type();
}
```

#### Encode data to TVC

```jshelllanguage
{
	ContractTvc tvc1 = template.tvc();
	ContractTvc tvc2 = ContractTvc.ofFile("~/MyContract.tvc");
	Credentials keys = Credentials.RANDOM(sdk);
	Map<String, Object> initialData = Map.of("initDataParam1", "helloWorld!"); // one static initData var
	ContractTvc tvc1update = tvc1.withUpdatedInitialData(sdk, template.abi(), initialData, keys.publicKey());
}
```
