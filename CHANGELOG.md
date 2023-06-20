### 1.8.0

- Added support of EVER-SDK 1.43.2 changes
- Better exceptions for FunctionHandle::callTreeAsMap
- Fixes for large map(type,tuple) conversions
- New sendFrom methods for easier sending internal messages from multisigs
- Updated README examples
- New builders for multisigs and TIP3 contracts

### 1.7.0

- Added FunctionHandle::getLocal and FunctionHandle::getLocalAsMap for getters run locally on downloadad bocs
- Added Contract::decodeMessageBoc to decode events and other messages produced by contract
- Fixed Address type serialization
- Improved output serialization
- Fixed getAsMap() NullPointerException
- Improved type conversion

### 1.6.2

- Fixed string ABI->Java decoding
- Fixed string refs not incrementing counter in TvmCell encoding
- Added helper methods for getting type names from AbiType objects
- Added TvmCell::decode and TvmCell::decodeAndGet for decoding bocs

### 1.6.1

- Updated code generation to fix method outputs problem
- Added output type conversion
- Added MessageFlag helper enum
- Added ResultOfTree::extractDeployAddress method to easily get new contract addresses
- Added some methods to TIP3 class for faster deploy and usage of TIP3 contracts
- Added easier deploy methods to MultisigWallet class
- Improved a lot of tests
- Added TIP3 tests
- Fixed problem with TvmBuilder::store for strings

### 1.6.0

- Updated EVER-SDK -> 1.42.1
- Fixed logging in DeployHandle
- Added FunctionHandle::runLocal and FunctionHandle::runLocalAsMap methods to for local/mock/fake execution of contract calls
- Added tests for OnchainConfig and LocalConfig
- Improved config serialization
- Added method Tvc::codeCell for creation of acquiring template code
- Added method FunctionHandle::toPayload for acquiring payload of function call
- Added deploy of CustomContract from CustomTemplate.
- Added method DeployHandle::withCredentials to change credentials in chain style

### 1.5.0

- Updated EVER-SDK -> 1.41
- Updated dependency: Gradle -> 8.0.2
- Removed dependency: org.junit.platform:junit-platform-launcher
- Added Contract Wrapper Generation API (creates contract and template wrappers from ABI json)
- Reworked java4ever API to work with generated wrappers
- Simplified writing custom wrappers

### 1.4.0

- Updated EVER-SDK -> 1.40
- Made large API rework
- Added transaction tree logging
- Added functionId() calculator
- Added builder for creating TVCs from scratch
- Improved type conversion between ABI and Java
- Added util methods for templates
- Updated dependency: Gradle -> 7.6
- Added transaction tree logging and exception handling

### 1.3.0

- Removed SLF4J facade
- Added SLF4J 2.0 bridge to JDK Platform Logging
- Added runLocal() method for Account class (to run contract methods locally in emulated blockchain)
- Added serialization/deserialization helpers to Sdk
- Switched to "api" type dependencies to export binding & jackson to library users
- Added parseStruct() method for faster struct parsing in ABI responses

### 1.2.0
