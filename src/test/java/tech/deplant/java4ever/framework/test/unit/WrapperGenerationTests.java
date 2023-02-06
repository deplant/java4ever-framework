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
	public void first_msig_deploy_passes_second_throws() throws IOException, EverSdkException {

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
				.addJavadoc(wrapperDocs.build())
				.addModifiers(Modifier.PUBLIC);
		wrapperBuilder.addRecordComponent(ContractHandle.class, "contract");
		//wrapperBuilder.superclass(OwnedContract.class);
		var wrapperConstructorBuilder = MethodSpec.constructorBuilder();
		// Sdk sdk, String address, ContractAbi abi, Credentials credentials
		wrapperConstructorBuilder
				.addModifiers(Modifier.PUBLIC)
				.addParameter(Sdk.class, "sdk")
				.addParameter(String.class, "address")
				.addParameter(ContractAbi.class, "abi")
				.addParameter(Credentials.class, "credentials")
				.addStatement("this(new ContractHandle(sdk, address, abi, credentials))");
		wrapperBuilder.addMethod(wrapperConstructorBuilder.build());

		final TypeSpec.Builder templateBuilder = TypeSpec
				.classBuilder(wrapperName + "Template")
				.addJavadoc(wrapperDocs.build())
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL);

		for (var func : abi.functions()) {
			if (Strings.notEmptyEquals(func.name(), "constructor")) {
				// constructor function is special, it goes to contract template too
				logger.log(System.Logger.Level.INFO, "constructor!");
			} else {
				// all other functions
				logger.log(System.Logger.Level.INFO, func.name());
				//if (func.outputs().length == 0) {
				var methodBuilder = MethodSpec.methodBuilder(func.name());
				methodBuilder.returns(CallHandle.class);
				methodBuilder.addModifiers(Modifier.PUBLIC);
				StringBuilder mapStringBuilder = new StringBuilder("$T params = $T.of(");
				List<String> mapParams = new ArrayList<>();
				List<Object> mapArgsBuilder = new ArrayList<>();
				mapArgsBuilder.add(ParameterizedTypeName.get(TypeName.MAP, TypeName.STRING, TypeName.OBJECT));
				mapArgsBuilder.add(TypeName.MAP);
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
				bodyBuilder.addStatement(mapStringBuilder.toString(), mapArgsBuilder.toArray());
				bodyBuilder.addStatement("return new $T(contract(), $S, params, null)", CallHandle.class, func.name());
				methodBuilder.addCode(bodyBuilder.build());
				//methodBuilder.addException(EverSdkException.class);
				wrapperBuilder.addMethod(methodBuilder.build());
				//}
			}

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
		var handle = new SafeMultisigWallet(null).acceptTransfer("");
		handle.call();
	}

}
