package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.SubscribeEvent;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.datatype.Uint;
import tech.deplant.java4ever.framework.gql.SubscribeHandle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Contract {

	static <IMPL> IMPL instantiate(Class<IMPL> clazz, Sdk sdk, String address, ContractAbi abi,
	                               Credentials credentials) {
		List<?> componentTypes = Stream
				.of(clazz.getRecordComponents())
				.map(RecordComponent::getType)
				.toList();
		for (Constructor<?> c : clazz.getDeclaredConstructors()) {
			if (Arrays.asList(c.getParameterTypes()).equals(componentTypes)) {
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
	}

	/**
	 * Check actual EVER balance on contract's account.
	 *
	 * @return
	 * @throws EverSdkException
	 */
	default BigInteger accountBalance() throws EverSdkException {
		return Uint.fromJava(128, account().balance()).toJava();
	}

	/**
	 * Downloads actual account info, including boc.
	 * Use account().boc() to get it.
	 *
	 * @return
	 * @throws EverSdkException
	 */
	default Account account() throws EverSdkException {
		return Account.ofAddress(sdk(), address());
	}

	Sdk sdk();

	String address();

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
		return account().tvmPubkey(sdk(), abi());
	}

	default FunctionHandle<Map<String, Object>> prepareCall(String functionName,
	                                                        Map<String, Object> functionInputs,
	                                                        Abi.FunctionHeader functionHeader) {
		return new FunctionHandle<>(sdk(),
		                            address(),
		                            abi(),
		                            credentials(),
		                            functionName,
		                            functionInputs,
		                            functionHeader);
	}

	default SubscribeHandle subscribeOnTransactions(String resultFields, Consumer<SubscribeEvent> subscribeEventConsumer) throws EverSdkException {
		final String queryText = """
				subscription {
							transactions(
									filter: {
										account_addr: { eq: "%s" }
									}
				                ) {
								%s
							}
						}
				""".formatted(address(),resultFields);
		return SubscribeHandle.subscribe(sdk(), queryText, subscribeEventConsumer);
	}

	default SubscribeHandle subscribeOnIncomingMessages(String resultFields, Consumer<SubscribeEvent> subscribeEventConsumer) throws EverSdkException {
		final String queryText = """
				subscription {
							messages(
									filter: {
										dst: { eq: "%s" }
									}
				                ) {
								%s
							}
						}
				""".formatted(address(),resultFields);
		return SubscribeHandle.subscribe(sdk(), queryText, subscribeEventConsumer);
	}

	default SubscribeHandle subscribeOnOutgoingMessages(String resultFields, Consumer<SubscribeEvent> subscribeEventConsumer) throws EverSdkException {
		final String queryText = """
				subscription {
							messages(
									filter: {
										src: { eq: "%s" }
									}
				                ) {
								%s
							}
						}
				""".formatted(address(),resultFields);
		return SubscribeHandle.subscribe(sdk(), queryText, subscribeEventConsumer);
	}

	default SubscribeHandle subscribeOnAccount(String resultFields, Consumer<SubscribeEvent> subscribeEventConsumer) throws EverSdkException {
		final String queryText = """
				subscription {
							accounts(
									filter: {
										id: { eq: "%s" }
									}
				                ) {
								%s
							}
						}
				""".formatted(address(),resultFields);
		return SubscribeHandle.subscribe(sdk(), queryText, subscribeEventConsumer);
	}

	default Abi.DecodedMessageBody decodeMessageBoc(TvmCell messageBoc) throws EverSdkException {
		return Abi.decodeMessage(sdk().context(),
		                         abi().ABI(),
		                         messageBoc.cellBoc(),
		                         false,
		                         null,
		                         null);
	}

}
