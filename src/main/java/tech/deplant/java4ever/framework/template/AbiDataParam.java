package tech.deplant.java4ever.framework.template;

import tech.deplant.java4ever.binding.Abi;

record AbiDataParam(int key, String name, String type, Abi.AbiParam[] components) {
}
