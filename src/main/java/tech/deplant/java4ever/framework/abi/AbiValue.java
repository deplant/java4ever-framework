package tech.deplant.java4ever.framework.abi;

import tech.deplant.java4ever.binding.EverSdkException;

public interface AbiValue {

	Object serialize() throws EverSdkException;

}
