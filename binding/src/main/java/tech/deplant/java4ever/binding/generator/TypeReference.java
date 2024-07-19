package tech.deplant.java4ever.binding.generator;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.commons.Strings;
import tech.deplant.java4ever.binding.SubscribeEvent;
import tech.deplant.java4ever.binding.ffi.EverSdkContext;
import tech.deplant.javapoet.*;
import tech.deplant.java4ever.binding.generator.jtype.SdkObject;
import tech.deplant.java4ever.binding.generator.reference.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public record TypeReference(String module,
                            String name,
                            boolean isArray,
                            boolean isOptional,
                            boolean isGeneric,
                            boolean isRef,
                            boolean isVoid) {

	private static final List<String> SPECIAL_REFS = List.of("Value", "API", "ClientContext");

	private final static System.Logger logger = System.getLogger(TypeReference.class.getName());

	public static TypeReference fromApiType(ApiType apiType) {
		return referenceRecursion(new TypeReference(null, null, false, false, false, false, false), apiType);
	}

	private static TypeReference referenceRecursion(TypeReference ref, ApiType apiType) {
		final TypeReference typeReference = switch (apiType) {
			case GenericType gen -> {
				if ("AppObject".equals(gen.generic_name())) {
					logger.log(System.Logger.Level.WARNING, () ->
					           "AppObject! : " + ref + " generic: " + gen);
				}
				yield referenceRecursion(ref.withIsGeneric(true), gen.generic_args()[0]);
			}
			case OptionalType opt -> referenceRecursion(ref.withIsOptional(true), opt.optional_inner());
			case ArrayType arr -> referenceRecursion(ref.withIsArray(true), arr.array_item());
			case RefType r -> {
				if (SPECIAL_REFS.contains(r.ref_name())) {
					// special case for BuilderOp.Integer
					if (Strings.isNotEmpty(apiType.summary()) &&
					    apiType.summary().contains("integer number")) {
						yield ref.withName("String").withIsRef(false);
					}
					yield ref.withName(r.ref_name()).withIsRef(false);
				} else {
					yield ref.withName(r.ref_name()).withIsRef(true);
				}
			}
			case NumberType num -> {
				if (num.number_size() >= 32) {
					yield ref.withName("Long").withIsRef(false);
				} else {
					yield ref.withName("Integer").withIsRef(false);
				}
			}
			case StringType str -> ref.withName("String").withIsRef(false);
			case BooleanType bool -> ref.withName("Boolean").withIsRef(false);
			case BigIntType bigInt -> ref.withName("BigInteger").withIsRef(false);
			case NoneType none -> ref.withIsVoid(true);
			default -> throw new IllegalStateException("Unexpected value: " + apiType);
		};
		if (typeReference != null &&
		    typeReference.module() == null &&
		    typeReference.isRef() &&
		    !typeReference.name().isEmpty()) {
			String[] splittedName = typeReference.name().split("\\.");
			if (splittedName.length < 2) {
				logger.log(System.Logger.Level.ERROR, () ->
						"Type reference without 'module.type' notation! " + typeReference);
				return typeReference;
			}
			// special case for Abi interface that messes with module name
			String refName = "Abi".equals(splittedName[1]) ? "ABI" : splittedName[1];
			return typeReference
					.withModule(ParserUtils.capitalize(splittedName[0]))
					.withName(refName);
		} else {
			return typeReference;
		}
	}

	public TypeReference withModule(String module) {
		return new TypeReference(module, name(), isArray(), isOptional(), isGeneric(), isRef(), isVoid());
	}

	public TypeReference withName(String name) {
		return new TypeReference(module(), name, isArray(), isOptional(), isGeneric(), isRef(), isVoid());
	}

	public TypeReference withIsArray(boolean isArray) {
		return new TypeReference(module(), name(), isArray, isOptional(), isGeneric(), isRef(), isVoid());
	}

	public TypeReference withIsOptional(boolean isOptional) {
		return new TypeReference(module(), name(), isArray(), isOptional, isGeneric(), isRef(), isVoid());
	}

	public TypeReference withIsGeneric(boolean isGeneric) {
		return new TypeReference(module(), name(), isArray(), isOptional(), isGeneric, isRef(), isVoid());
	}

	public TypeReference withIsRef(boolean isRef) {
		return new TypeReference(module(), name(), isArray(), isOptional(), isGeneric(), isRef, isVoid());
	}

	public TypeReference withIsVoid(boolean isVoid) {
		return new TypeReference(module(), name(), isArray(), isOptional(), isGeneric(), isRef(), isVoid);
	}

	public TypeName toTypeName() {
		TypeName typeName;
		if (isRef()) {
			typeName = switch (name()) {
				case "Request" -> {
					logger.log(System.Logger.Level.WARNING,  () ->
							"Callback!");
					yield ClassName.get(SubscribeEvent.class);
				}
				default -> ClassName.bestGuess(module() + "." + name());
			};
		} else {
			typeName = switch (name()) {
				case "Integer" -> ClassName.get(Integer.class);
				case "String" -> TypeName.STRING;
				case "Boolean" -> ClassName.get(Boolean.class);
				case "Long" -> ClassName.get(Long.class);
				case "BigInteger" -> ClassName.get(BigInteger.class);
				case "Value", "API" -> ClassName.get(JsonNode.class);//ParameterizedTypeName.get(TypeName.MAP, TypeName.STRING, TypeName.OBJECT);
				case "ClientContext" -> ClassName.get(EverSdkContext.class);
				default -> throw new IllegalStateException("Unexpected value: " + name());
			};
		}
		if (isArray()) {
			typeName = ArrayTypeName.of(typeName);
		}
		return typeName;
	}

	public SdkObject toTypeDeclaration(Map<ParserEngine.SdkType, SdkObject> typeLibrary) {
		return typeLibrary.get(toSdkType());
	}

	public ParserEngine.SdkType toSdkType() {
		return new ParserEngine.SdkType(module(), name());
	}
}
