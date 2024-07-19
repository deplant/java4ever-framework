package tech.deplant.java4ever.binding.generator.jtype;

import tech.deplant.javapoet.TypeSpec;
import tech.deplant.java4ever.binding.generator.reference.ApiType;

public interface SdkObject {

	TypeSpec.Builder poeticize();

	String name();

	ApiType type();

	boolean isSimpleWrapper();

	boolean isFlatType();

	boolean isStructure();

	boolean isParams();

	boolean isResult();
}
