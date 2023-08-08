package tech.deplant.java4ever.frtest.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.LocalConfig;
import tech.deplant.java4ever.framework.OnchainConfig;
import tech.deplant.java4ever.framework.contract.AbstractContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tech.deplant.java4ever.frtest.unit.Env.INIT;
import static tech.deplant.java4ever.frtest.unit.Env.SDK_EMPTY;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class ConfigTests {

	@BeforeAll
	public static void init_sdk_and_other_vars() throws IOException, EverSdkException {
		INIT();
	}

	@Test
	public void serialized_then_deserialized_onchain_configs_are_equal() throws IOException, EverSdkException {
		var keys = Credentials.RANDOM(SDK_EMPTY);
		var contract = new AbstractContract(SDK_EMPTY, Address.ZERO.toString(), SafeMultisigWalletTemplate.DEFAULT_ABI());
		var conf = OnchainConfig.EMPTY("config/onchain-config.json");
		conf.addKeys("test_keys", keys);
		conf.addContract("test_contract", contract);
		conf = OnchainConfig.LOAD("config/onchain-config.json");
		assertEquals(keys.publicKey(), conf.keys("test_keys").publicKey());
		assertEquals(contract.abi().json(), conf.contract(AbstractContract.class, SDK_EMPTY, "test_contract").abi().json());
	}

	@Test
	public void serialized_then_deserialized_local_configs_are_equal() throws IOException, EverSdkException {
var local = LocalConfig.EMPTY("config/local-config.json", "C:/everscale/TON-Solidity-Compiler/build/solc/Release/solc",
		                             "C:/everscale/TVM-linker/target/release/tvm_linker",
		                             "C:/everscale/TON-Solidity-Compiler/lib/stdlib_sol.tvm",
		                             "C:/idea_git/iris-contracts-core/src/main/solidity",
		                             "C:/idea_git/iris-contracts-core/src/main/resources/artifacts");

		local = LocalConfig.LOAD("config/local-config.json");
		assertEquals("C:/everscale/TVM-linker/target/release/tvm_linker", local.info().linker().linkerPath());
	}

}
