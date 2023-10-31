package tech.deplant.java4ever.framework.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.DeployHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.contract.tip4.TIP4WalletContract;

/**
 * Java template class for deploy of <strong>TIP4WalletContract</strong> contract for Everscale blockchain.
 */
public record TIP4WalletTemplate(ContractAbi abi, Tvc tvc) implements Template {
  public TIP4WalletTemplate(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public TIP4WalletTemplate() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.2\",\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"sendTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"flags\",\"type\":\"uint8\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"transferOwnership\",\"inputs\":[{\"name\":\"newOwner\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]},{\"name\":\"owner\",\"inputs\":[],\"outputs\":[{\"name\":\"owner\",\"type\":\"uint256\"}]},{\"name\":\"_randomNonce\",\"inputs\":[],\"outputs\":[{\"name\":\"_randomNonce\",\"type\":\"uint256\"}]}],\"events\":[{\"name\":\"OwnershipTransferred\",\"inputs\":[{\"name\":\"previousOwner\",\"type\":\"uint256\"},{\"name\":\"newOwner\",\"type\":\"uint256\"}]}],\"data\":[{\"key\":1,\"name\":\"_randomNonce\",\"type\":\"uint256\"}],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"owner\",\"type\":\"uint256\"},{\"name\":\"_randomNonce\",\"type\":\"uint256\"}]}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("te6ccgECGAEAAuQAAgE0AwEBAcACAEPQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgBCSK7VMg4wMgwP/jAiDA/uMC8gsVBQQXArztRNDXScMB+GYh2zzTAAGOHIMI1xgg+QEB0wABlNP/AwGTAvhC4iD4ZfkQ8qiV0wAB8nri0z8B+EMhufK0IPgjgQPoqIIIG3dAoLnytPhj0x8B+CO88rnTHwHbPPI8CAYDSu1E0NdJwwH4ZiLQ1wsDqTgA3CHHAOMCIdcNH/K8IeMDAds88jwUFAYCKCCCEFhakGS74wIgghBotV8/uuMCCQcDUjD4Qm7jAPhG8nPR+EUgbpIwcN74Qrry5E/4APhFIG6SMHDe2zzbPPIACA0LAWLtRNDXScIBjiZw7UTQ9AVwcSKAQPQOb5GT1wv/3vhr+GqAQPQO8r3XC//4YnD4Y+MNEwRQIIIQEXjpvbrjAiCCEDtTMx+64wIgghBM7mRsuuMCIIIQWFqQZLrjAhIRDgoDNjD4RvLgTPhCbuMAIZPU0dDe0//R2zww2zzyABMMCwAs+Ev4SvhD+ELIy//LP8+Dy//L/8ntVAEs+EUgbpIwcN74Srry5E0g8uRO+ADbPA0ARvhKIfhqjQRwAAAAAAAAAAAAAAAAFNs0/KDIzsv/y//JcPsAA0Iw+Eby4Ez4Qm7jACGT1NHQ3vpA03/SANMH1NHbPOMA8gATEA8AKO1E0NP/0z8x+ENYyMv/yz/Oye1UAFT4RSBukjBw3vhKuvLkTfgAVQJVEsjPhYDKAM+EQM4B+gJxzwtqzMkB+wABUDDR2zz4SyGOHI0EcAAAAAAAAAAAAAAAAC7UzMfgyM7L/8lw+wDe8gATAVAw0ds8+EohjhyNBHAAAAAAAAAAAAAAAAAkXjpvYMjOy//JcPsA3vIAEwAu7UTQ0//TP9MAMdP/0//R+Gv4avhj+GIACvhG8uBMAgr0pCD0oRcWABRzb2wgMC42Mi4wAAA=");
  }

  public DeployHandle<TIP4WalletContract> prepareDeploy(Sdk sdk, Credentials credentials,
      BigInteger _randomNonce) {
    Map<String, Object> initialDataFields = Map.of("_randomNonce", _randomNonce);
    Map<String, Object> params = Map.of();
    return new DeployHandle<TIP4WalletContract>(TIP4WalletContract.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
