package tech.deplant.java4ever.binding.generator.jtype;

import tech.deplant.javapoet.TypeSpec;
import tech.deplant.java4ever.binding.generator.reference.ApiType;

public record SdkDummy(ApiType type) implements SdkObject {
	@Override
	public TypeSpec.Builder poeticize() {
		return null;
	}

	@Override
	public String name() {
		return type().name();
	}

	@Override
	public boolean isSimpleWrapper() {
		return false;
	}

	@Override
	public boolean isFlatType() {
		return true;
	}

	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public boolean isParams() {
		return false;
	}

	@Override
	public boolean isResult() {
		return false;
	}
}
