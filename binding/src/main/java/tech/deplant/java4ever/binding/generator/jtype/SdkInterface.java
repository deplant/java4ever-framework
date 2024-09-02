package tech.deplant.java4ever.binding.generator.jtype;

import tech.deplant.javapoet.TypeSpec;
import tech.deplant.java4ever.binding.generator.reference.ApiType;
import tech.deplant.java4ever.binding.generator.reference.EnumOfTypes;

import javax.lang.model.element.Modifier;
import java.util.List;

public record SdkInterface(EnumOfTypes eot,
                           String name,
                           SdkDocs javadoc,
                           List<SdkRecord> children) implements SdkObject {

	@Override
	public TypeSpec.Builder poeticize() {

		TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(name())
		                                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.SEALED);
		for (SdkRecord child : this.children) {
			interfaceBuilder.addType(child.poeticize().build());
		}

		return interfaceBuilder
				.addJavadoc(this.javadoc.poeticize().build());
	}

	@Override
	public ApiType type() {
		return eot();
	}

	@Override
	public boolean isSimpleWrapper() {
		return false;
	}

	@Override
	public boolean isFlatType() {
		return false;
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
