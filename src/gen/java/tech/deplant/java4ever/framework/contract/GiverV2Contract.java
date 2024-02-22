package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.math.BigInteger;
import java.util.Map;

/**
 * Java wrapper class for usage of <strong>GiverV2Contract</strong> contract for Everscale blockchain.
 */
public class GiverV2Contract extends GiverContract {
	public GiverV2Contract(int sdk, String address) throws JsonProcessingException {
		super(sdk, address, DEFAULT_ABI(), Credentials.NONE);
	}

	public GiverV2Contract(int sdk, String address, ContractAbi abi) {
		super(sdk, address, abi, Credentials.NONE);
	}

	public GiverV2Contract(int sdk, String address, Credentials credentials) throws
			JsonProcessingException {
		super(sdk, address, DEFAULT_ABI(), credentials);
	}

	@JsonCreator
	public GiverV2Contract(int sdk, String address, ContractAbi abi, Credentials credentials) {
		super(sdk, address, abi, credentials);
	}

	public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
		return ContractAbi.ofString(
				"{\"ABI version\":2,\"header\":[\"time\",\"expire\"],\"functions\":[{\"name\":\"upgrade\",\"inputs\":[{\"name\":\"newcode\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"sendTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"}],\"outputs\":[]},{\"name\":\"getMessages\",\"inputs\":[],\"outputs\":[{\"name\":\"messages\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"hash\",\"type\":\"uint256\"},{\"name\":\"expireAt\",\"type\":\"uint64\"}]}]},{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]}],\"events\":[]}");
	}

	public FunctionHandle<Void> upgrade(TvmCell newcode) {
		Map<String, Object> params = Map.of("newcode", newcode);
		return new FunctionHandle<Void>(Void.class, sdk(), address(), abi(), credentials(), "upgrade", params, null);
	}

	public FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce) {
		Map<String, Object> params = Map.of("dest", dest,
		                                    "value", value,
		                                    "bounce", bounce);
		return new FunctionHandle<Void>(Void.class,
		                                sdk(),
		                                address(),
		                                abi(),
		                                credentials(),
		                                "sendTransaction",
		                                params,
		                                null);
	}

	public FunctionHandle<ResultOfGetMessages> getMessages() {
		Map<String, Object> params = Map.of();
		return new FunctionHandle<ResultOfGetMessages>(ResultOfGetMessages.class,
		                                               sdk(),
		                                               address(),
		                                               abi(),
		                                               credentials(),
		                                               "getMessages",
		                                               params,
		                                               null);
	}

	public record ResultOfGetMessages(Map<String, Object>[] messages) {
	}
}
