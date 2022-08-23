package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.IAbi;
import tech.deplant.java4ever.framework.artifact.Artifact;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.type.Address;

import java.io.IOException;
import java.util.Map;

public interface IContract {

    Sdk sdk();

    Address address();

    IAbi abi();

    Credentials tvmKey();

    Account account();

    Map<String, Object> runGetter(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials);

    Map<String, Object> callExternal(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials);

    String encodeInternal(Address dest, String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader);

    interface Cacheable {
        void save(Artifact artifact) throws IOException;
    }


    interface Owned extends IContract {
        Map<String, Object> callExternal(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader);

        Map<String, Object> runGetter(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader);
    }

    interface KnownTVC {

    }

    interface ExternallyDeployable {

    }
}
