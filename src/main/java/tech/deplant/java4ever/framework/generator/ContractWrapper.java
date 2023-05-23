package tech.deplant.java4ever.framework.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.generator.ParserUtils;
import tech.deplant.java4ever.binding.generator.javapoet.*;
import tech.deplant.java4ever.framework.*;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.SolStruct;
import tech.deplant.java4ever.framework.datatype.TvmBuilder;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.Template;
import tech.deplant.java4ever.utils.Objs;
import tech.deplant.java4ever.utils.Strings;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ContractWrapper {

	private static System.Logger logger = System.getLogger(ContractWrapper.class.getName());

	private static TypeName typeSwitch(String abiTypeString) throws EverSdkException {
		var details = SolStruct.typeParser(abiTypeString);
		TypeName resultTypeName = switch (details.type()) {
			case INT, UINT -> {
				if (details.size() <= 32) {
					yield ClassName.get(Integer.class);
				} else if (details.size() <= 64) {
					yield ClassName.get(Long.class);
				} else {
					yield ClassName.get(BigInteger.class);
				}
			}
			case STRING, BYTE, BYTES -> TypeName.STRING;
			case ADDRESS -> ClassName.get(Address.class);
			case BOOL -> ClassName.get(Boolean.class);
			case CELL -> ClassName.get(TvmCell.class);
			case SLICE -> TypeName.STRING; //TODO Slices aren't implemented!!!
			case BUILDER -> ClassName.get(TvmBuilder.class);
			case TUPLE -> ParameterizedTypeName.get(TypeName.MAP, TypeName.STRING, TypeName.OBJECT);
		};
		if (details.isArray()) {
			resultTypeName = ArrayTypeName.of(resultTypeName);
		}
		return resultTypeName;
	}

	private static TypeName toTypeName(String abiTypeString) throws EverSdkException {

		String typeStringPattern = "([a-zA-Z]+\\d{0,3}\\[?\\]?)";

		var mapPattern = Pattern.compile("(map\\()" +
		                                 typeStringPattern +
		                                 "(,)" +
		                                 typeStringPattern +
		                                 "(\\))");
		boolean rootIsMap = false;
		String rootTypeString = abiTypeString;
		String keyTypeString = null;
		String valueTypeString = null;

		var matcher = mapPattern.matcher(rootTypeString);
		while (matcher.find()) {
			rootIsMap = true;
			keyTypeString = matcher.group(2);
			valueTypeString = matcher.group(4);
		}

		if (rootIsMap) {
			var keyName = typeSwitch(keyTypeString);
			var valueName = typeSwitch(valueTypeString);
			return ParameterizedTypeName.get(ClassName.get(Map.class), keyName, valueName);
		} else {
			var typeName = typeSwitch(abiTypeString);
			return typeName;
		}
	}

	public static void generate(Abi.AbiContract abi,
	                            Tvc tvc,
	                            Path targetDirectory,
	                            String contractName,
	                            String wrapperPackage,
	                            String templatePackage,
	                            String[] superInterfaces) throws IOException, EverSdkException {

		boolean hasTvc = Objs.isNotNull(tvc);

		String wrapperName = ParserUtils.capitalize(contractName);

		var wrapperDocs = CodeBlock
				.builder()
				.add(String.format("""
						                   Java wrapper class for usage of <strong>%s</strong> contract for Everscale blockchain.
						                   """,
				                   wrapperName));

		var templateDocs = CodeBlock
				.builder()
				.add(String.format("""
						                   Java template class for deploy of <strong>%s</strong> contract for Everscale blockchain.
						                   """,
				                   wrapperName));

		final TypeSpec.Builder wrapperBuilder = TypeSpec
				.recordBuilder(wrapperName)
				.addJavadoc(wrapperDocs.build())
				.addModifiers(Modifier.PUBLIC);

		if (Objs.isNull(superInterfaces) || superInterfaces.length == 0) {
			wrapperBuilder.addSuperinterface(Contract.class);
		} else {
			for (var s : superInterfaces) {
				wrapperBuilder.addSuperinterface(ClassName.bestGuess(s));
			}
		}

		wrapperBuilder.addRecordComponent(Sdk.class, "sdk");
		wrapperBuilder.addRecordComponent(String.class, "address");
		wrapperBuilder.addRecordComponent(ContractAbi.class, "abi");
		wrapperBuilder.addRecordComponent(Credentials.class, "credentials");

		wrapperBuilder.addMethod(MethodSpec.constructorBuilder()
		                                   .addStatement("this(sdk,address,DEFAULT_ABI(),Credentials.NONE)")
		                                   .addParameter(Sdk.class, "sdk")
		                                   .addParameter(String.class, "address")
		                                   .addException(JsonProcessingException.class)
		                                   .addModifiers(Modifier.PUBLIC)
		                                   .build());

		wrapperBuilder.addMethod(MethodSpec.constructorBuilder()
		                                   .addStatement("this(sdk,address,abi,Credentials.NONE)")
		                                   .addParameter(Sdk.class, "sdk")
		                                   .addParameter(String.class, "address")
		                                   .addParameter(ContractAbi.class, "abi")
		                                   .addModifiers(Modifier.PUBLIC)
		                                   .build());

		wrapperBuilder.addMethod(MethodSpec.constructorBuilder()
		                                   .addStatement("this(sdk,address,DEFAULT_ABI(),credentials)")
		                                   .addParameter(Sdk.class, "sdk")
		                                   .addParameter(String.class, "address")
		                                   .addParameter(Credentials.class, "credentials")
		                                   .addException(JsonProcessingException.class)
		                                   .addModifiers(Modifier.PUBLIC)
		                                   .build());

		final TypeSpec.Builder templateBuilder = TypeSpec
				.recordBuilder(wrapperName + "Template")
				.addSuperinterface(Template.class)
				.addJavadoc(templateDocs.build())
				.addModifiers(Modifier.PUBLIC);

		templateBuilder.addRecordComponent(ContractAbi.class, "abi");
		templateBuilder.addRecordComponent(Tvc.class, "tvc");

		var defaultAbiFunction = MethodSpec.methodBuilder("DEFAULT_ABI")
		                                   .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
		                                   .returns(ClassName.get(ContractAbi.class))
		                                   .addException(JsonProcessingException.class)
		                                   .addStatement("return ContractAbi.ofString($S)",
		                                                 ContextBuilder.DEFAULT_MAPPER.setSerializationInclusion(
				                                                               JsonInclude.Include.NON_NULL)
		                                                                              .writeValueAsString(abi))
		                                   .build();
		wrapperBuilder.addMethod(defaultAbiFunction);
		templateBuilder.addMethod(defaultAbiFunction);

		var tvcOnlyConstructorBuilder = MethodSpec.constructorBuilder();
		tvcOnlyConstructorBuilder
				.addStatement("this(DEFAULT_ABI(), tvc)")
				.addParameter(ParameterSpec.builder(Tvc.class, "tvc").build())
				.addModifiers(Modifier.PUBLIC)
				.addException(JsonProcessingException.class);
		templateBuilder.addMethod(tvcOnlyConstructorBuilder.build());

		if (hasTvc) {
			templateBuilder.addMethod(MethodSpec.methodBuilder("DEFAULT_TVC")
			                                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
			                                    .returns(ClassName.get(Tvc.class))
			                                    .addStatement("return Tvc.ofBase64String($S)", tvc.base64String())
			                                    .build());

			var noArgsConstructorBuilder = MethodSpec.constructorBuilder();
			noArgsConstructorBuilder
					.addStatement("this(DEFAULT_ABI(),DEFAULT_TVC())")
					.addModifiers(Modifier.PUBLIC)
					.addException(JsonProcessingException.class);
			templateBuilder.addMethod(noArgsConstructorBuilder.build());
		}
		//public static final Tvc SAFE_MULTISIG_TVC = Tvc.ofResource(
		//		"artifacts/multisig/SafeMultisigWallet.tvc");

		for (var func : abi.functions()) {
			MethodSpec.Builder methodBuilder = null;
			boolean isConstructor = Strings.notEmptyEquals(func.name(), "constructor");
			if (isConstructor) {
				methodBuilder = MethodSpec.methodBuilder("prepareDeploy");
				logger.log(System.Logger.Level.INFO, "constructor!");
				methodBuilder.addParameter(ParameterSpec.builder(Sdk.class, "sdk").build());
				//methodBuilder.addParameter(ParameterSpec.builder(Integer.class, "workchainId").build());
				methodBuilder.addParameter(ParameterSpec.builder(Credentials.class, "credentials").build());


				StringBuilder fieldsMapBuilder = new StringBuilder("$T initialDataFields = $T.of(");
				List<String> fieldsList = new ArrayList<>();
				List<Object> fieldArgsList = new ArrayList<>();
				fieldArgsList.add(ParameterizedTypeName.get(TypeName.MAP, TypeName.STRING, TypeName.OBJECT));
				fieldArgsList.add(TypeName.MAP);

				if (Objs.isNotNull(abi.data())) {
					for (var field : abi.data()) {
						TypeName typeName = toTypeName(field.type());
						var paramSpec = ParameterSpec.builder(typeName, field.name()).build();
						methodBuilder.addParameter(paramSpec);
						fieldsList.add("$S, $N");
						fieldArgsList.add(field.name());
						fieldArgsList.add(paramSpec);
					}
				}
				fieldsMapBuilder.append(String.join(", \n", fieldsList));
				fieldsMapBuilder.append(")");
				var bodyBuilder = CodeBlock.builder();
				bodyBuilder.addStatement(fieldsMapBuilder.toString(), fieldArgsList.toArray());
				methodBuilder.addCode(bodyBuilder.build());

			} else {
				methodBuilder = MethodSpec.methodBuilder(func.name());
			}
			// all other functions
			logger.log(System.Logger.Level.INFO, func.name());
			//if (func.outputs().length == 0) {
			methodBuilder.addModifiers(Modifier.PUBLIC);
			StringBuilder mapStringBuilder = new StringBuilder("$T params = $T.of(");
			List<String> mapParams = new ArrayList<>();
			List<Object> mapArgsBuilder = new ArrayList<>();
			mapArgsBuilder.add(ParameterizedTypeName.get(TypeName.MAP, TypeName.STRING, TypeName.OBJECT));
			mapArgsBuilder.add(TypeName.MAP);

			TypeSpec resultOfFunctionType = null;
			if (func.outputs().length > 0) {

				final TypeSpec.Builder resultTypeBuilder = TypeSpec
						.recordBuilder("ResultOf" + ParserUtils.capitalize(func.name()))
						.addModifiers(Modifier.PUBLIC);

				for (var param : func.outputs()) {
					TypeName resultTypeName = toTypeName(param.type());
					var paramSpec = ParameterSpec.builder(resultTypeName, param.name()).build();
					resultTypeBuilder.addRecordComponent(paramSpec);
				}
				resultOfFunctionType = resultTypeBuilder.build();
				wrapperBuilder.addType(resultOfFunctionType);
			}

			for (var param : func.inputs()) {
				if (param.name().equals("answerId")) {
					mapParams.add("\"answerId\", 0");
				} else {
					TypeName typeName = toTypeName(param.type());
					var paramSpec = ParameterSpec.builder(typeName, param.name()).build();
					methodBuilder.addParameter(paramSpec);
					mapParams.add("$S, $N");
					mapArgsBuilder.add(param.name());
					mapArgsBuilder.add(paramSpec);
				}
			}
			mapStringBuilder.append(String.join(", \n", mapParams));
			mapStringBuilder.append(")");
			var bodyBuilder = CodeBlock.builder();
			bodyBuilder.addStatement(mapStringBuilder.toString(), mapArgsBuilder.toArray());

			ClassName handleTypeName;
			ClassName handleParamTypeName;

			if (isConstructor) {
				handleTypeName = ClassName.get(DeployHandle.class);
				handleParamTypeName = ClassName.get(wrapperPackage, wrapperName);
			} else {
				handleTypeName = ClassName.get(FunctionHandle.class);
				if (Objs.isNull(resultOfFunctionType)) {
					handleParamTypeName = ClassName.get(Void.class);
				} else {
					handleParamTypeName = ClassName.bestGuess(resultOfFunctionType.name);
				}
			}
			var resultName = ParameterizedTypeName.get(handleTypeName, handleParamTypeName);
			methodBuilder.returns(resultName);

			if (isConstructor) {
				bodyBuilder.addStatement(
						"return new $T($T.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null)",
						resultName,
						handleParamTypeName);
				methodBuilder.addCode(bodyBuilder.build());
				templateBuilder.addMethod(methodBuilder.build());
			} else {
				bodyBuilder.addStatement("return new $T($T.class, sdk(), address(), abi(), credentials(), $S, params, null)",
				                         resultName,
				                         handleParamTypeName,
				                         func.name());
				methodBuilder.addCode(bodyBuilder.build());
				wrapperBuilder.addMethod(methodBuilder.build());
			}
		}

		// file writing loop
		JavaFile contractFile = JavaFile
				.builder(wrapperPackage, wrapperBuilder.build())
				.build();
		contractFile.writeTo(targetDirectory);

		JavaFile templateFile = JavaFile
				.builder(templatePackage, templateBuilder.build())
				.build();
		templateFile.writeTo(targetDirectory);
	}
}
