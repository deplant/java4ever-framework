module java4ever.framework {
	requires jdk.incubator.concurrent;
	requires transitive java4ever.binding;
	requires transitive deplant.commons;
	requires transitive deplant.javapoet;
	requires java.compiler;
	requires com.fasterxml.jackson.datatype.jdk8;
	requires com.fasterxml.jackson.module.paramnames;
	requires com.fasterxml.jackson.datatype.jsr310;
	exports tech.deplant.java4ever.framework;
	exports tech.deplant.java4ever.framework.artifact;
	exports tech.deplant.java4ever.framework.contract;
	exports tech.deplant.java4ever.framework.template;
	exports tech.deplant.java4ever.framework.datatype;
	exports tech.deplant.java4ever.framework.generator;
	opens tech.deplant.java4ever.framework to com.fasterxml.jackson.databind;
	opens tech.deplant.java4ever.framework.contract to com.fasterxml.jackson.databind;
	opens tech.deplant.java4ever.framework.template to com.fasterxml.jackson.databind;
	opens tech.deplant.java4ever.framework.generator to com.fasterxml.jackson.databind;
	opens tech.deplant.java4ever.framework.datatype to com.fasterxml.jackson.databind;
	opens tech.deplant.java4ever.framework.artifact to com.fasterxml.jackson.databind;
	exports tech.deplant.java4ever.framework.contract.multisig;
	opens tech.deplant.java4ever.framework.contract.multisig to com.fasterxml.jackson.databind;
	exports tech.deplant.java4ever.framework.contract.tip3;
	opens tech.deplant.java4ever.framework.contract.tip3 to com.fasterxml.jackson.databind;
	exports tech.deplant.java4ever.framework.gql;
	opens tech.deplant.java4ever.framework.gql to com.fasterxml.jackson.databind;
	//exports tech.deplant.java4ever.framework.contract.multisig;
	//opens tech.deplant.java4ever.framework.contract.multisig to com.fasterxml.jackson.databind;
	//exports tech.deplant.java4ever.framework.contract.tip3;
	//opens tech.deplant.java4ever.framework.contract.tip3 to com.fasterxml.jackson.databind;

}