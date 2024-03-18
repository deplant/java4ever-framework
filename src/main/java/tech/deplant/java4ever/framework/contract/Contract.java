package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.datatype.Uint;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

public interface Contract {

	static System.Logger logger = System.getLogger(Contract.class.getName());

	static <IMPL extends AbstractContract> IMPL instantiate(Class<IMPL> clazz,
	                                                        int sdk,
	                                                        String address,
	                                                        ContractAbi abi,
	                                                        Credentials credentials) {
		//List<?> componentTypes = Stream.of(clazz.getRecordComponents()).map(RecordComponent::getType).toList();
		for (Constructor<?> c : clazz.getDeclaredConstructors()) {
			if (Arrays.equals(c.getParameterTypes(),
			                  new Class<?>[]{Integer.class, String.class, ContractAbi.class, Credentials.class})) {
				try {
					return (IMPL) c.newInstance(sdk, address, abi, credentials);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
				         InvocationTargetException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;

/*		Map<String, Object> parameters = Map.of("sdk", sdk,
		                                        "address", address,
		                                        "abi", abi,
		                                        "credentials", credentials);

		return ABI_JSON_MAPPER().convertValue(parameters, clazz);*/
	}

	/**
	 * Check actual EVER balance on contract's account.
	 *
	 * @return
	 * @throws EverSdkException
	 */
	default BigInteger accountBalance() throws EverSdkException {
		return Uint.of(128, account().balance()).toJava();
	}

	/**
	 * Downloads actual account info, including boc.
	 * Use account().boc() to get it.
	 *
	 * @return
	 * @throws EverSdkException
	 */
	default Account account() throws EverSdkException {
		return Account.ofAddress(contextId(), address());
	}

	int contextId();

	Address address();

//	default String addressString() {
//		return address().makeAddrStd();
//	}

	ContractAbi abi();

	/**
	 * Credentials that were provided in object constructor. They can be different from real pubkey
	 * inside contract's inside contract's initialData. To check real pubkey in account, use
	 * tvmPubkey() method.
	 *
	 * @return
	 */
	Credentials credentials();

	/**
	 * Returns actual tvm.pubkey() of smart contract. If you want to get Credentials specified at
	 * OwnedContract constructor - use credentials() method.
	 *
	 * @return
	 * @throws EverSdkException
	 */
	default String tvmPubkey() throws EverSdkException {
		return account().tvmPubkey(contextId(), abi());
	}

	default FunctionHandle<Map<String, Object>> prepareCall(String functionName,
	                                                        Map<String, Object> functionInputs,
	                                                        Abi.FunctionHeader functionHeader) {
		return new FunctionHandle<>(contextId(),
		                            address(),
		                            abi(),
		                            credentials(),
		                            functionName,
		                            functionInputs,
		                            functionHeader);
	}

	default Abi.DecodedMessageBody decodeMessageBoc(TvmCell messageBoc) throws EverSdkException {
		return Abi.decodeMessage(contextId(), abi().ABI(), messageBoc.cellBoc(), false, null, null);
	}

}
