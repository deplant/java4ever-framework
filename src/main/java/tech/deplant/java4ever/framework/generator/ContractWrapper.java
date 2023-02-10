package tech.deplant.java4ever.framework.generator;

import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.generator.ParserUtils;
import tech.deplant.java4ever.binding.generator.javapoet.*;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.abi.ContractAbi;
import tech.deplant.java4ever.framework.abi.datatype.Address;
import tech.deplant.java4ever.framework.abi.datatype.TvmBuilder;
import tech.deplant.java4ever.framework.abi.datatype.TvmCell;
import tech.deplant.java4ever.framework.artifact.JsonResource;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.contract.DeployCall;
import tech.deplant.java4ever.framework.contract.FunctionCall;
import tech.deplant.java4ever.framework.contract.Template;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.framework.template.ContractTvc;
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
		var details = ContractAbi.typeParser(abiTypeString);
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

	public static void generate(String resourceName,
	                            Path targetDirectory,
	                            String contractName,
	                            String wrapperPackage,
	                            String templatePackage) throws IOException, EverSdkException {

		var abi = ContextBuilder.DEFAULT_MAPPER.readValue(
				new JsonResource(resourceName).get(),
				Abi.AbiContract.class
		);

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
				.addSuperinterface(Contract.class)
				.addJavadoc(wrapperDocs.build())
				.addModifiers(Modifier.PUBLIC);
		wrapperBuilder.addRecordComponent(Sdk.class, "sdk");
		wrapperBuilder.addRecordComponent(String.class, "address");
		wrapperBuilder.addRecordComponent(ContractAbi.class, "abi");
		wrapperBuilder.addRecordComponent(Credentials.class, "credentials");

		final TypeSpec.Builder templateBuilder = TypeSpec
				.recordBuilder(wrapperName + "Template")
				.addSuperinterface(Template.class)
				.addJavadoc(templateDocs.build())
				.addModifiers(Modifier.PUBLIC);

		templateBuilder.addRecordComponent(ContractAbi.class, "abi");
		templateBuilder.addRecordComponent(ContractTvc.class, "tvc");

		for (var func : abi.functions()) {
			MethodSpec.Builder methodBuilder = null;
			boolean isConstructor = Strings.notEmptyEquals(func.name(), "constructor");
			if (isConstructor) {
				methodBuilder = MethodSpec.methodBuilder("deploy");
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
				TypeName typeName = toTypeName(param.type());
				var paramSpec = ParameterSpec.builder(typeName, param.name()).build();
				methodBuilder.addParameter(paramSpec);
				mapParams.add("$S, $N");
				mapArgsBuilder.add(param.name());
				mapArgsBuilder.add(paramSpec);
			}
			mapStringBuilder.append(String.join(", \n", mapParams));
			mapStringBuilder.append(")");
			var bodyBuilder = CodeBlock.builder();
			bodyBuilder.addStatement(mapStringBuilder.toString(), mapArgsBuilder.toArray());

			ClassName handleTypeName;
			ClassName handleParamTypeName;

			if (isConstructor) {
				handleTypeName = ClassName.get(DeployCall.class);
				handleParamTypeName = ClassName.get(wrapperPackage, wrapperName);
			} else {
				handleTypeName = ClassName.get(FunctionCall.class);
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
						"return new $T(sdk, this, sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null)",
						resultName);
				methodBuilder.addCode(bodyBuilder.build());
				templateBuilder.addMethod(methodBuilder.build());
			} else {
				bodyBuilder.addStatement("return new $T(this, $S, params, null)", resultName, func.name());
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
