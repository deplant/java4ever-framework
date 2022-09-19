package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.binding.Abi;

record AbiFunction(String name, String id, Abi.AbiParam[] inputs, Abi.AbiParam[] outputs) {
}
