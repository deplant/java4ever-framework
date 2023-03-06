### 1.5.0

- Updated EVER-SDK -> 1.41
- - Updated dependency: Gradle -> 8.0.2
- Added Contract Wrapper Generation API (creates contract and template wrappers from ABI json)
- Reworked java4ever API to work with generated wrappers
- Simplified writing custom wrappers

### 1.4.0

- Updated EVER-SDK -> 1.40
- Made large API rework
- Added transaction tree logging
- Added functionId() calculator
- Added util methods for templates
- Updated dependency: Gradle -> 7.6

### 1.3.0

- Removed SLF4J facade
- Added SLF4J 2.0 bridge to JDK Platform Logging
- Added runLocal() method for Account class (to run contract methods locally in emulated blockchain)
- Added serialization/deserialization helpers to Sdk
- Switched to "api" type dependencies to export binding & jackson to library users
- Added parseStruct() method for faster struct parsing in ABI responses

### 1.2.0