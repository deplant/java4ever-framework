package tech.deplant.java4ever.framework.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.datatype.Address;

import java.math.BigInteger;

public interface EverOSGiver extends Giver {


	System.Logger logger = System.getLogger(tech.deplant.java4ever.framework.contract.EverOSGiver.class.getName());

	static GiverV2 V2(Sdk sdk) throws JsonProcessingException {
		return new GiverV2(sdk,
		                   "0:ece57bcc6c530283becbbd8a3b24d3c5987cdddc3c8b7b33be6e4a6312490415",
		                   GiverV2.DEFAULT_ABI(),
		                   new Credentials("2ada2e65ab8eeab09490e3521415f45b6e42df9c760a639bcf53957550b25a16",
		                                   "172af540e43a524763dd53b26a066d472a97c4de37d5498170564510608250c3"));
	}

	@Override
	default FunctionHandle<Void> give(String to, BigInteger value) {
		logger.log(System.Logger.Level.INFO, "Giver called!");
		return sendTransaction(new Address(to), value, false);
	}

	FunctionHandle<Void> sendTransaction(Address dest, BigInteger value, Boolean bounce);
}
