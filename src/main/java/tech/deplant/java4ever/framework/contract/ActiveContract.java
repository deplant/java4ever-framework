package tech.deplant.java4ever.framework.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.Processing;
import tech.deplant.java4ever.binding.Tvm;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.IAbi;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.type.Address;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

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
        return Abi.encodeMessageBody(
                this.sdk.context(),
                this.abi.ABI(),
                new Abi.CallSet(functionName, functionHeader, convertInputs(functionName, functionInputs)),
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
        convertInputs(functionName, functionInputs);
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
        convertInputs(functionName, functionInputs);
        return processMessage(this.abi, this.address, null, credentials, functionName, null, functionInputs);
    }

    private Map<String, Object> convertInputs(String functionName, Map<String, Object> functionInputs) {
        return functionInputs.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey(), entry -> {
                    if (this.abi.hasInput(functionName, entry.getKey())) {
                        var type = this.abi.inputType(functionName, entry.getKey());
                        return switch (type) {
                            case "uint128", "uint256", "uint64", "uint32" -> switch (entry.getValue()) {
                                case BigInteger b -> "0x" + b.toString(16);
                                case Instant i -> "0x" + BigInteger.valueOf(i.getEpochSecond()).toString(16);
                                case String s
                                        when"0x".equals(s.substring(0, 2)) -> s;
                                case String s -> "0x" + s;
                                default -> entry.getValue();
                            };
                            case "address" -> switch (entry.getValue()) {
                                case Address a -> a.makeAddrStd();
                                default -> entry.getValue();
                            };
                            default -> entry.getValue();
                        };
                    } else {
                        log.error("Function " + functionName + " doesn't contain input (" + entry.getKey() + ") in ABI of " + this.address.makeAddrStd());
                        return null;
                    }
                }
        ));
    }

}
