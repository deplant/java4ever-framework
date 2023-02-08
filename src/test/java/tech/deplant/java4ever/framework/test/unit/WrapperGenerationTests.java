package tech.deplant.java4ever.framework.test.unit;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
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
import tech.deplant.java4ever.framework.contract.CallHandle;
import tech.deplant.java4ever.framework.contract.ContractHandle;
import tech.deplant.java4ever.framework.contract.SafeMultisigWallet;
import tech.deplant.java4ever.framework.crypto.Credentials;
import tech.deplant.java4ever.utils.Objs;
import tech.deplant.java4ever.utils.Strings;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class WrapperGenerationTests {

	private static System.Logger logger = System.getLogger(CredentialsTests.class.getName());

	@Test
	public void generate() throws IOException, EverSdkException {

		String contractName = "SafeMultisigWallet";
		String resourceName = "artifacts/multisig/SafeMultisigWallet.abi.json";

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
				.addSuperinterface(ContractHandle.class)
				.addJavadoc(wrapperDocs.build())
				.addModifiers(Modifier.PUBLIC);
		wrapperBuilder.addRecordComponent(Sdk.class, "sdk");
		wrapperBuilder.addRecordComponent(String.class, "address");
		wrapperBuilder.addRecordComponent(ContractAbi.class, "abi");
		wrapperBuilder.addRecordComponent(Credentials.class, "credentials");

		final TypeSpec.Builder templateBuilder = TypeSpec
				.recordBuilder(wrapperName + "Template")
				.addJavadoc(templateDocs.build())
				.addModifiers(Modifier.PUBLIC);

		for (var func : abi.functions()) {
			MethodSpec.Builder methodBuilder = null;
			boolean isConstructor = Strings.notEmptyEquals(func.name(), "constructor");
			if (isConstructor) {
				methodBuilder = MethodSpec.methodBuilder("deploy");
				logger.log(System.Logger.Level.INFO, "constructor!");
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
					var typeDetails = ContractAbi.typeParser(param.type());
					//logger.log(System.Logger.Level.INFO, );
					TypeName resultTypeName = switch (typeDetails.type()) {
						case INT, UINT -> {
							if (typeDetails.size() <= 32) {
								yield ClassName.get(Integer.class);
							} else if (typeDetails.size() <= 64) {
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
					if (typeDetails.isArray()) {
						resultTypeName = ArrayTypeName.of(resultTypeName);
					}
					var paramSpec = ParameterSpec.builder(resultTypeName, param.name()).build();
					resultTypeBuilder.addRecordComponent(paramSpec);
				}

				resultOfFunctionType = resultTypeBuilder.build();

				wrapperBuilder.addType(resultOfFunctionType);
			}

			for (var param : func.inputs()) {
				var typeDetails = ContractAbi.typeParser(param.type());
				//logger.log(System.Logger.Level.INFO, );
				TypeName typeName = switch (typeDetails.type()) {
					case INT, UINT -> {
						if (typeDetails.size() <= 32) {
							yield ClassName.get(Integer.class);
						} else if (typeDetails.size() <= 64) {
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
				if (typeDetails.isArray()) {
					typeName = ArrayTypeName.of(typeName);
				}
				var paramSpec = ParameterSpec.builder(typeName, param.name()).build();
				methodBuilder.addParameter(paramSpec);
				mapParams.add("$S, $N");
				mapArgsBuilder.add(param.name());
				mapArgsBuilder.add(paramSpec);
			}
			mapStringBuilder.append(String.join(", \n", mapParams));
			mapStringBuilder.append(")");
			var bodyBuilder = CodeBlock.builder();
			TypeName callHandleType = Objs.isNull(resultOfFunctionType) ? ParameterizedTypeName.get(CallHandle.class,
			                                                                                        Void.class) : ParameterizedTypeName.get(
					ClassName.get(CallHandle.class),
					ClassName.bestGuess(resultOfFunctionType.name));
			bodyBuilder.addStatement(mapStringBuilder.toString(), mapArgsBuilder.toArray());
			bodyBuilder.addStatement("return new $T(this, $S, params, null)", callHandleType, func.name());
			methodBuilder.addCode(bodyBuilder.build());

			//methodBuilder.addException(EverSdkException.class);
			if (isConstructor) {
				methodBuilder.returns(ClassName.bestGuess(wrapperName));
				templateBuilder.addMethod(methodBuilder.build());
			} else {
				methodBuilder.returns(callHandleType);
				wrapperBuilder.addMethod(methodBuilder.build());
			}

			//}
		}

		// file writing loop
		JavaFile contractFile = JavaFile
				.builder("tech.deplant.java4ever.framework.contract", wrapperBuilder.build())
				.build();
		contractFile.writeTo(Paths.get("src/gen/java"));

		JavaFile templateFile = JavaFile
				.builder("tech.deplant.java4ever.framework.template", templateBuilder.build())
				.build();
		templateFile.writeTo(Paths.get("src/gen/java"));
	}

	@Test
	public void test_generated() throws EverSdkException {
		var handle = new SafeMultisigWallet(null, null, null, null).getParameters();
		var result = handle.call();
	}

}
