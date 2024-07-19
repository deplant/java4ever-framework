package tech.deplant.java4ever.binding.generator.jtype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tech.deplant.commons.Objs;
import tech.deplant.java4ever.binding.generator.ParserEngine;
import tech.deplant.javapoet.*;
import tech.deplant.java4ever.binding.generator.reference.ApiType;
import tech.deplant.java4ever.binding.generator.reference.StructType;

import javax.lang.model.element.Modifier;
import java.util.Map;

public record SdkRecord(StructType originalType,
                        String name,
                        ParserEngine.SdkInterfaceParent superInterface,
                        boolean isSimpleWrapper,
                        boolean isParams,
                        boolean isResult,
                        Map<ParserEngine.SdkType, SdkObject> typeLibrary) implements SdkObject {


	private final static System.Logger logger = System.getLogger(SdkRecord.class.getName());

	public static SdkRecord ofApiType(StructType struct,
	                                  Map<ParserEngine.SdkType, SdkObject> typeLibrary,
	                                  ParserEngine.SdkInterfaceParent superInterface) {
		boolean isParams = false;
		boolean isResult = false;
		if (struct.name().length() >= 8) {
			isParams = "ParamsOf".equals(struct.name().substring(0, 8));
			isResult = "ResultOf".equals(struct.name().substring(0, 8));
		}
		boolean isSimpleWrapper = struct.struct_fields().length == 1;
		return new SdkRecord(struct,
		                     struct.name(),
		                     superInterface,
		                     isSimpleWrapper,
		                     isParams,
		                     isResult,
		                     typeLibrary);
	}

	@Override
	public TypeSpec.Builder poeticize() {
		// METADATA
		TypeSpec.Builder structBuilder = TypeSpec
				.recordBuilder(this.originalType.name())
				.addModifiers(Modifier.PUBLIC);
		// RECORD PARAMS
		for (ApiType component : this.originalType.struct_fields()) {
			structBuilder
					.addRecordComponent(SdkParam.ofApiType(component, typeLibrary()).poeticize().build());
		}
		// JAVADOC
		structBuilder.addJavadoc(new SdkDocs(originalType().summary(), originalType().description()).poeticize()
		                                                                                            .build());
		// for records that are implementing interfaces of EnumOfTypes
		if (Objs.isNotNull(superInterface())) {
			// if record is a subtype of EnumOfTypes
			// it should implement super interface
			structBuilder.addSuperinterface(ClassName.bestGuess(superInterface().name()));
			// and have special type() getter
			//structBuilder.addMethod(virtualTypeField(originalType().name()).build());
			structBuilder.addMethod(virtualTypeField(superInterface().variantName()).build());
		}

		// for AbiEvent we should add special Jackson annotation to ignore 'outputs' tag.
		// It shouldn't be deserialized as it's deprecated and unused,
		// but still it's presented in multisig contracts, so we should not throw error.
		if ("AbiEvent".equals(name())) {
			structBuilder.addAnnotation(AnnotationSpec.builder(JsonIgnoreProperties.class)
			                                          .addMember("value", "$S", "outputs")
			                                          .build());
		}

		return structBuilder;
	}

	@Override
	public ApiType type() {
		return originalType();
	}

	@Override
	public boolean isFlatType() {
		return false;
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	/**
	 * Method that generates virtual type() getter that is used to generate correct JSON.
	 *
	 * @param typeName stringified name of subclass
	 * @return builder of getter method spec
	 */
	private MethodSpec.Builder virtualTypeField(String typeName) {
		return MethodSpec
				.methodBuilder("type")
				.addModifiers(Modifier.PUBLIC)
				.returns(ClassName.STRING)
				.addStatement("return \"" + typeName + "\"")
				.addAnnotation(SdkParam.renamedFieldAnnotation("type"));
	}

	public SdkRecord withSuperInterface(ParserEngine.SdkInterfaceParent superInterface) {
		return new SdkRecord(
				originalType(),
				name(),
				superInterface,
				isSimpleWrapper(),
				isParams(),
				isResult(),
				typeLibrary()
		);
	}
}
