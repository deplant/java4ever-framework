package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;

import java.math.BigInteger;
import java.util.Map;

public class EverWalletContract extends GiverContract {

	public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
		return ContractAbi.ofString(
				"""
						{
						  "ABI version": 2,
						  "version": "2.3",
						  "header": [
						    "pubkey",
						    "time",
						    "expire"
						  ],
						  "functions": [
						    {
						      "name": "sendTransaction",
						      "inputs": [
						        {
						          "name": "dest",
						          "type": "address"
						        },
						        {
						          "name": "value",
						          "type": "uint128"
						        },
						        {
						          "name": "bounce",
						          "type": "bool"
						        },
						        {
						          "name": "flags",
						          "type": "uint8"
						        },
						        {
						          "name": "payload",
						          "type": "cell"
						        }
						      ],
						      "outputs": []
						    },
						    {
						      "name": "sendTransactionRaw",
						      "inputs": [
						        {
						          "name": "flags",
						          "type": "uint8"
						        },
						        {
						          "name": "message",
						          "type": "cell"
						        }
						      ],
						      "outputs": []
						    }
						  ],
						  "data": [],
						  "events": [],
						  "fields": [
						    {
						      "name": "_pubkey",
						      "type": "uint256"
						    },
						    {
						      "name": "_timestamp",
						      "type": "uint64"
						    }
						  ]
						}
						""");
	}

	/**
	 * Instantiates a new EVER Wallet contract.
	 *
	 * @param sdk         the sdk
	 * @param address     the address
	 * @param credentials the credentials
	 */
	public EverWalletContract(int sdk, Address address, Credentials credentials) throws JsonProcessingException {
		super(sdk, String.valueOf(address), DEFAULT_ABI(), credentials);
	}

	/**
	 * Instantiates a new EVER WAllet contract.
	 *
	 * @param sdk         the sdk
	 * @param address     the address
	 * @param signer the credentials
	 */
	public EverWalletContract(int sdk, Address address, Abi.Signer signer) throws JsonProcessingException {
		super(sdk, String.valueOf(address), DEFAULT_ABI(), signer);
	}

	@Override
	public FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce) {
		Map<String, Object> inputMap = Map.of("dest",
		                                      dest,
		                                      "value",
		                                      value,
		                                      // amount in nano EVER
		                                      "bounce",
		                                      bounce,
		                                      "flags",
		                                      3,
		                                      "payload",
		                                      TvmCell.EMPTY.cellBoc());
		return new FunctionHandle<Void>(Void.class,
		                                this,
		                                "sendTransaction",
		                                inputMap,
		                                null);
	}
}
