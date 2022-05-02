package tech.deplant.java4ever.framework;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class AccountController {
    @Getter
    @Setter
    private Account account;
    @Getter
    @Setter
    private Credentials externalOwner;
    @Getter
    @Setter
    private AccountController internalOwner;

    public AccountController(Account account, Credentials externalOwner, AccountController internalOwner) {
        this.account = account;
        this.externalOwner = externalOwner;
        this.internalOwner = internalOwner;
    }

    public static AccountController ofAddress(ContractAbi abi, Address address, Sdk sdk, Credentials externalOwner, AccountController internalOwner) throws Sdk.SdkException {
        return new AccountController(Account.ofAddress(sdk, address, abi), externalOwner, internalOwner);
    }

    public Map<String, Object> callExternalFromOwner(@NonNull String functionName, Map<String, Object> functionInputs) throws Sdk.SdkException {
        convertInputs(functionName, functionInputs);
        Map<String, Object> result = this.account.callExternal(this.externalOwner, functionName, functionInputs);
        this.account(refreshAcc());
        return result;
    }


    public Map<String, Object> callInternalFromCustom(AccountController customSender, @NonNull String functionName, Map<String, Object> functionInputs, BigInteger functionValue, boolean functionBounce) throws Sdk.SdkException {
        if (this.account.abi().hasFunction(functionName)) {
            convertInputs(functionName, functionInputs);
            Map<String, Object> result = this.account.callInternalFromMsig(
                    customSender.externalOwner(),
                    customSender.account().address(),
                    functionValue,
                    functionName,
                    functionValue,
                    functionInputs,
                    functionBounce
            );
            this.account(refreshAcc());
            return result;
        } else {
            log.error(() -> "Function (" + functionName + ") not found in ABI of " + this.account.address().makeAddrStd());
            return new HashMap<>();
        }
    }

    public Map<String, Object> callInternalFromOwner(@NonNull String functionName, Map<String, Object> functionInputs, BigInteger functionValue, boolean functionBounce) throws IOException, Sdk.SdkException {
        return callInternalFromCustom(this.internalOwner, functionName, functionInputs, functionValue, functionBounce);
    }

    public Map<String, Object> runGetter(@NonNull String abiFunction, Map<String, Object> input) {
        convertInputs(abiFunction, input);
        return this.account.runGetter(abiFunction, input);
    }

    public void refresh() throws Sdk.SdkException {
        this.account(refreshAcc());
    }

    private Account refreshAcc() throws Sdk.SdkException {
        return Account.ofAddress(this.account.sdk(), this.account.address(), this.account.abi());
    }

    private void convertInputs(String functionName, Map<String, Object> functionInputs) {
        if (functionInputs != null) {
            functionInputs.forEach((key, value) ->
                    {
                        if (this.account.abi().hasInput(functionName, key)) {
                            var type = this.account.abi().inputType(functionName, key);
                            functionInputs.replace(key,
                                    switch (type) {
                                        case "uint128", "uint256", "uint64", "uint32" -> switch (value) {
                                            case BigInteger b -> "0x" + b.toString(16);
                                            case Instant i -> "0x" + BigInteger.valueOf(i.getEpochSecond()).toString(16);
                                            case String s && "0x".equals(s.substring(0, 2)) -> s;
                                            case String s -> "0x" + s;
                                            default -> value;
                                        };
                                        case "address" -> switch (value) {
                                            case Address a -> a.makeAddrStd();
                                            default -> value;
                                        };
                                        default -> value;
                                    }
                            );
                        } else {
                            log.error(() -> "Function " + functionName + " doesn't contain input (" + key + ") in ABI of " + this.account.address().makeAddrStd());
                        }
                    }
            );
        }
    }

}
