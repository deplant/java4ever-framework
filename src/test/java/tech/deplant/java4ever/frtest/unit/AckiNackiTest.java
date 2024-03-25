package tech.deplant.java4ever.frtest.unit;

import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.framework.Account;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.contract.multisig.MultisigWalletGoshContract;
import tech.deplant.java4ever.framework.contract.multisig.SafeMultisigWalletContract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.Uint;
import tech.deplant.java4ever.framework.template.MultisigWalletGoshTemplate;
import tech.deplant.java4ever.framework.template.SafeMultisigWalletTemplate;

import java.io.IOException;
import java.math.BigInteger;

import static tech.deplant.java4ever.frtest.unit.Env.EVER_ONE;

public class AckiNackiTest {

    final static String GOSH_ENDPOINT = "https://ackinacki-testnet.tvmlabs.dev/graphql";

    @Test
    public void gosh_multisig_deploy_test() throws IOException, EverSdkException {
        var goshGiverAddress = new Address("0:c5bdf82533a8d5f89a2e15f567090f722f2ba711e6944051ab24f1bbe51465ce");
        var giverKeys = new Credentials("9aa86533452795d4f65a8fa7f74744a304d57d3f0f9f72ef1ff46b9f47f9c71e",
                "f935f680c23ddfd0a0c334b1b6d64465a4dbbf00e0ec54010b7bb60c0328f0e6");

        EverSdk.load(new AbsolutePathLoader("C:/opt/gosh-sdk/ton_client.dll"));

        var sdkGosh = EverSdk.builder()
                .networkEndpoints(GOSH_ENDPOINT)
                .networkQueryTimeout(300_000L)
                .build()
                .orElseThrow();
        var goshGiver = new SafeMultisigWalletContract(sdkGosh, String.valueOf(goshGiverAddress), giverKeys);


        var myKeys = Credentials.RANDOM(sdkGosh);
        System.out.println("Keys: " + myKeys);
        var deployStatement = new MultisigWalletGoshTemplate().prepareDeploy(sdkGosh,0,
                myKeys,
                new BigInteger[]{myKeys.publicBigInt()},
                1);

        System.out.println("Address: " + deployStatement.toAddress());
        var goshDeployContract = deployStatement.deployWithGiver(goshGiver, EVER_ONE);

        //Keys: Credentials[
        //publicKey=fd40b2bfdf25fc48afb2e7b5c078ce00581e871dcfdb0ea185299baf06bbfbaf,
        //secretKey=4487eedb7f474c5ee245da4458c60c3ec0fbc05532800ee3f5197a2668011eb4
        // ]
        //Address: 0:f9ac2b07f6441eeb01606fb1ff69b498dc6f3f267fa639fb2f51da91cb8c7075

        System.out.println("Balance: " + new Uint(goshDeployContract.account().balance()).toJava());
    }

}
