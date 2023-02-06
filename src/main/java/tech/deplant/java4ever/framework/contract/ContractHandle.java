package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.crypto.Credentials;

public record ContractHandle(Sdk sdk,
                             String address,
                             ContractAbi abi,
                             Credentials credentials) {
}
