package tech.deplant.java4ever.framework.abi.type;

import java.util.List;

public record AbiParam(String name, AbiValueType type, int size, List<AbiParam> elements) {
}
