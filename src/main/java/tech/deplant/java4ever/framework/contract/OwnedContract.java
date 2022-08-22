package tech.deplant.java4ever.framework.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.IAbi;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.type.Address;

import java.util.Map;

public class OwnedContract implements IContract.Owned {

    private static Logger log = LoggerFactory.getLogger(OwnedContract.class);
    private final IContract contract;
    private final Credentials tvmKey;

    public OwnedContract(IContract contract, Credentials tvmKey) {
        this.contract = contract;
        this.tvmKey = tvmKey;
    }

    public OwnedContract(Sdk sdk, Address address, IAbi abi, Credentials tvmKey) {
        this.contract = new ActiveContract(sdk, address, abi);
        this.tvmKey = tvmKey;
    }

    @Override
    public Sdk sdk() {
        return this.contract.sdk();
    }

    @Override
    public Address address() {
        return this.contract.address();
    }

    @Override
    public IAbi abi() {
        return this.contract.abi();
    }

    @Override
    public Account account() {
        return this.contract.account();
    }

    @Override
    public Credentials tvmKey() {
        return this.tvmKey;
    }

    @Override
    public Map<String, Object> runGetter(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials) {
        return this.contract.runGetter(functionName, functionInputs, functionHeader, credentials);
    }

    @Override
    public Map<String, Object> callExternal(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials) {
        return this.contract.callExternal(functionName, functionInputs, functionHeader, credentials);
    }

    @Override
    public String encodeInternal(Address dest, String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        return this.contract.encodeInternal(dest, functionName, functionInputs, functionHeader);
    }


    @Override
    public Map<String, Object> callExternal(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        return this.contract.callExternal(functionName, functionInputs, functionHeader, this.tvmKey);
    }

    @Override
    public Map<String, Object> runGetter(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        return this.contract.runGetter(functionName, functionInputs, functionHeader, this.tvmKey);
    }
}
