package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdk;
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

/**
 * The interface Contract.
 */
public interface Contract {

	/**
	 * The constant logger.
	 */
	static System.Logger logger = System.getLogger(Contract.class.getName());

	/**
	 * Instantiate .
	 *
	 * @param <IMPL>      the type parameter
	 * @param clazz       the clazz
	 * @param sdk         the sdk
	 * @param address     the address
	 * @param abi         the abi
	 * @param credentials the credentials
	 * @return the
	 */
	static <IMPL extends AbstractContract> IMPL instantiate(Class<IMPL> clazz,
	                                                        int sdk,
	                                                        String address,
	                                                        ContractAbi abi,
	                                                        Credentials credentials) {
		//List<?> componentTypes = Stream.of(clazz.getRecordComponents()).map(RecordComponent::getType).toList();
		for (Constructor<?> c : clazz.getDeclaredConstructors()) {
			if (Arrays.equals(c.getParameterTypes(),
			                  new Class<?>[]{Integer.TYPE, String.class, ContractAbi.class, Credentials.class})) {
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
	 * @return big integer
	 * @throws EverSdkException the ever sdk exception
	 */
	default BigInteger accountBalance() throws EverSdkException {
		return Uint.of(128, account().balance()).toJava();
	}

	/**
	 * Downloads actual account info, including boc.
	 * Use account().boc() to get it.
	 *
	 * @return account
	 * @throws EverSdkException the ever sdk exception
	 */
	default Account account() throws EverSdkException {
		return Account.ofAddress(contextId(), address());
	}

	/**
	 * Context id int.
	 *
	 * @return the int
	 */
	int contextId();

	/**
	 * Address address.
	 *
	 * @return the address
	 */
	Address address();

//	default String addressString() {
//		return address().makeAddrStd();
//	}

	/**
	 * Abi contract abi.
	 *
	 * @return the contract abi
	 */
	ContractAbi abi();

	/**
	 * Credentials that were provided in object constructor. They can be different from real pubkey
	 * inside contract's inside contract's initialData. To check real pubkey in account, use
	 * tvmPubkey() method.
	 *
	 * @return credentials
	 */
	Credentials credentials();

	Abi.Signer signer();

//	/**
//	 * Returns actual tvm.pubkey() of smart contract. If you want to get Credentials specified at
//	 * OwnedContract constructor - use credentials() method.
//	 *
//	 * @return
//	 * @throws EverSdkException
//	 */
//	default String tvmPubkey() throws EverSdkException {
//		return account().tvmPubkey(contextId(), abi());
//	}

	/**
	 * Prepare call function handle.
	 *
	 * @param functionName   the function name
	 * @param functionInputs the function inputs
	 * @param functionHeader the function header
	 * @return the function handle
	 */
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

	/**
	 * Decode message boc abi . decoded message body.
	 *
	 * @param messageBoc the message boc
	 * @return the abi . decoded message body
	 * @throws EverSdkException the ever sdk exception
	 */
	default Abi.DecodedMessageBody decodeMessageBoc(TvmCell messageBoc) throws EverSdkException {
		return EverSdk.await(Abi.decodeMessage(contextId(), abi().ABI(), messageBoc.cellBoc(), false, null, null));
	}

}
