package tech.deplant.java4ever.framework.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.binding.Tvm;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.IAbi;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.type.Address;

import java.util.Map;

public record ActiveContract(Sdk sdk, Address address, IAbi abi) implements IContract {

    private static Logger log = LoggerFactory.getLogger(ActiveContract.class);

    @Override
    public Credentials tvmKey() {
        return null;
    }

    @Override
    public Account account() {
        return Account.graphQLRequest(this.sdk, this.address);
    }

    @Override
    public String encodeInternal(Address dest, String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader) {
        var inputs = abi().convertInputs(functionName, functionInputs);
        return Abi.encodeMessageBody(
                this.sdk.context(),
                this.abi.ABI(),
                new Abi.CallSet(functionName, functionHeader, inputs),
                true,
                Credentials.NONE.signer(),
                null,
                dest.makeAddrStd()
        ).body();

    }

    protected Map<String, Object> processMessage(IAbi abi, Address address, Abi.DeploySet deploySet, Credentials credentials, String functionName, Abi.FunctionHeader functionHeader, Map<String, Object> functionInputs) {
        Processing.ResultOfProcessMessage result = Processing.processMessage(this.sdk.context(),
                abi.ABI(),
                address.makeAddrStd(),
                deploySet,
                new Abi.CallSet(functionName, functionHeader, functionInputs),
                credentials.signer(), null, false, null);
        return decodeOutputMessage(result.decoded());
    }

    public Map<String, Object> decodeOutputMessage(Processing.DecodedOutput decoded) {
        return decoded.output();
    }

    @Override
    public Map<String, Object> runGetter(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials) {
        abi().convertInputs(functionName, functionInputs);
        Abi.ResultOfEncodeMessage msg =
                Abi.encodeMessage(
                        sdk().context(),
                        this.abi.ABI(),
                        this.address.makeAddrStd(),
                        null,
                        new Abi.CallSet(
                                functionName,
                                null,
                                functionInputs
                        ),
                        credentials.signer(),
                        null
                );

        Tvm.ResultOfRunTvm tvmExecuteResult = Tvm.runTvm(
                sdk().context(),
                msg.message(),
                account().boc(),
                null,
                this.abi.ABI(),
                null,
                false
        );
        return decodeOutputMessage(tvmExecuteResult.decoded());
    }

    @Override
    public Map<String, Object> callExternal(String functionName, Map<String, Object> functionInputs, Abi.FunctionHeader functionHeader, Credentials credentials) {
        abi().convertInputs(functionName, functionInputs);
        return processMessage(this.abi, this.address, null, credentials, functionName, null, functionInputs);
    }

}
