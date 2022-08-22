package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.IAbi;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.type.Address;

import java.util.Map;

public record ActiveContract(Sdk sdk, Address address, IAbi abi) implements IContract {
    @Override
    public Credentials tvmKey() {
        return null;
    }

    @Override
    public Account account() {
        return null;
    }

    @Override
    public Map<String, Object> runGetter(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        return null;
    }

    @Override
    public Map<String, Object> runGetter(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials) {
        return null;
    }

    @Override
    public Map<String, Object> callExternal(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        return null;
    }

    @Override
    public Map<String, Object> callExternal(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials) {
        return null;
    }

    @Override
    public String encodeInternal(Address dest, String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        return null;
    }
}
