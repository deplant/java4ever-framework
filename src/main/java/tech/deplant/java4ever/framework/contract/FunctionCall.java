package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.type.TypeReference;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.Convert;
import tech.deplant.java4ever.framework.LogUtils;
import tech.deplant.java4ever.framework.abi.datatype.Uint;
import tech.deplant.java4ever.framework.crypto.Credentials;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNullElse;
import static tech.deplant.java4ever.framework.LogUtils.info;

public record FunctionCall<RETURN>(Contract contract,
                                   String functionName,
                                   Map<String, Object> functionInputs,
                                   Abi.FunctionHeader functionHeader) {

	private static System.Logger logger = System.getLogger(FunctionCall.class.getName());

	public FunctionCall<RETURN> withContract(Contract contract) {
		return new FunctionCall<>(contract, functionName(), functionInputs(), functionHeader());
	}

	public FunctionCall<RETURN> withFunctionName(String functionName) {
		return new FunctionCall<>(contract(), functionName, functionInputs(), functionHeader());
	}

	public FunctionCall<RETURN> withFunctionInputs(Map<String, Object> functionInputs) {
		return new FunctionCall<>(contract(), functionName(), functionInputs, functionHeader());
	}

	public FunctionCall<RETURN> withFunctionHeader(Abi.FunctionHeader functionHeader) {
		return new FunctionCall<>(contract(), functionName(), functionInputs(), functionHeader);
	}

	public <T> FunctionCall<T> withReturnClass(Class<T> returnClass) {
		return new FunctionCall<>(contract(), functionName(), functionInputs(), functionHeader());
	}

	public Abi.CallSet toCallSet() throws EverSdkException {
		return new Abi.CallSet(functionName(),
		                       functionHeader(),
		                       contract().abi().convertFunctionInputs(functionName(), functionInputs()));
	}

	public Abi.Signer sign() {
		return requireNonNullElse(contract().credentials(), Credentials.NONE).signer();
	}

	public RETURN get() throws EverSdkException {
		Map<String, Object> filter = new HashMap<>();
		filter.put("id", new Account.GraphQLFilter.In(new String[]{contract().address()}));
		Net.ResultOfQueryCollection result = Net.queryCollection(contract().sdk().context(),
		                                                         "accounts",
		                                                         filter,
		                                                         "id acc_type balance boc data data_hash code code_hash init_code_hash last_paid",
		                                                         null,
		                                                         null);
		Abi.ResultOfEncodeMessage msg =
				Abi.encodeMessage(
						contract().sdk().context(),
						contract().abi().ABI(),
						contract().address(),
						null,
						toCallSet(),
						sign(),
						null
				);
		String boc = result.result()[0].get("boc").toString();
		var map = Optional.ofNullable(Tvm.runTvm(
				                                 contract().sdk().context(),
				                                 msg.message(),
				                                 boc,
				                                 null,
				                                 contract().abi().ABI(),
				                                 null,
				                                 false).decoded()
		                                 .output()).orElse(new HashMap<>());
		return contract().sdk().convertMap(map, new TypeReference<>() {
		});
	}

	public RETURN call() throws EverSdkException {
		var resultOfProcess = processExternalCall();
		var balanceDeltaStr = Convert.hexToDec(resultOfProcess.transaction().get("balance_delta").toString(), 9);
		Supplier<String> lazyFormatLogMessage = () -> String.format(LogUtils.CALL_LOG_BLOCK,
		                                                            "EXTERNAL CALL",
		                                                            this.functionName,
		                                                            resultOfProcess.transaction().get("id").toString(),
		                                                            resultOfProcess.transaction()
		                                                                           .get("in_msg")
		                                                                           .toString(),
		                                                            "ext",
		                                                            new BigDecimal(Uint.fromJava(128,
		                                                                                         resultOfProcess.fees()
		                                                                                                        .totalFwdFees())
		                                                                               .toJava(), 9)
				                                                            .toPlainString(),
		                                                            resultOfProcess.transaction()
		                                                                           .get("account_addr")
		                                                                           .toString(),
		                                                            0,
		                                                            this.functionName,
		                                                            new BigDecimal(Uint.fromJava(128,
		                                                                                         resultOfProcess.fees()
		                                                                                                        .totalAccountFees())
		                                                                               .toJava(), 9)
				                                                            .toPlainString(),
		                                                            "");
		info(logger, lazyFormatLogMessage);
		var map = Optional.ofNullable(resultOfProcess
				                              .decoded()
				                              .output()).orElse(new HashMap<>());

		return contract().sdk().convertMap(map, new TypeReference<>() {
		});
	}

	private Processing.ResultOfProcessMessage processExternalCall() throws EverSdkException {
		return Processing.processMessage(contract().sdk().context(),
		                                 contract().abi().ABI(),
		                                 contract().address(),
		                                 null,
		                                 toCallSet(),
		                                 sign(),
		                                 null,
		                                 false);
	}

}
