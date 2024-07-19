package tech.deplant.java4ever.binding.generator.jtype;

import com.fasterxml.jackson.annotation.JsonValue;
import tech.deplant.javapoet.MethodSpec;
import tech.deplant.javapoet.TypeSpec;
import tech.deplant.javapoet.TypeVariableName;
import tech.deplant.java4ever.binding.generator.reference.ApiType;
import tech.deplant.java4ever.binding.generator.reference.EnumOfConsts;

import javax.lang.model.element.Modifier;

public record SdkEnum(String name,
                      EnumOfConsts.Const[] consts,
                      SdkDocs javadoc) implements SdkObject {

	@Override
	public TypeSpec.Builder poeticize() {
		TypeSpec.Builder enumBuilder =
				TypeSpec.enumBuilder(this.name)
				        .addModifiers(Modifier.PUBLIC);
		boolean isNumericEnum = false;
		for (EnumOfConsts.Const enumValue : consts()) {
			if ("Number".equals(enumValue.type())) {
				isNumericEnum = true;
				enumBuilder.addEnumConstant(enumValue.name(),
				                            TypeSpec.anonymousClassBuilder("$L", enumValue.value())
				                                    .build());
			} else {
				enumBuilder.addEnumConstant(enumValue.name());
			}
		}
		if (isNumericEnum) {
			enumBuilder.addField(TypeVariableName.get("Integer"),
			                     "value",
			                     Modifier.PRIVATE,
			                     Modifier.FINAL)
			           .addMethod(MethodSpec.constructorBuilder()
			                                .addParameter(TypeVariableName.get("Integer"), "value")
			                                .addStatement("this.$N = $N", "value", "value")
			                                .build())
			           .addMethod(MethodSpec.methodBuilder("value")
					                      .addAnnotation(JsonValue.class)
			                                .addModifiers(Modifier.PUBLIC)
			                                .addStatement("return this.value")
			                                .returns(TypeVariableName.get("Integer"))
			                                .build());

		}
		//refs.put(en.parameterName(), en);
		return enumBuilder
				.addJavadoc(this.javadoc.poeticize().build());
	}

	@Override
	public ApiType type() {
		return null;
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
