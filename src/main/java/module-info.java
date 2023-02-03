module java4ever.framework {
	requires jdk.incubator.concurrent;
	requires transitive java4ever.binding;
	requires transitive java4ever.utils;
	//requires transitive com.fasterxml.jackson.databind;
	exports tech.deplant.java4ever.framework;
	exports tech.deplant.java4ever.framework.abi;
	exports tech.deplant.java4ever.framework.artifact;
	exports tech.deplant.java4ever.framework.contract;
	exports tech.deplant.java4ever.framework.template;
	exports tech.deplant.java4ever.framework.crypto;
	opens tech.deplant.java4ever.framework.contract to com.fasterxml.jackson.databind;
	opens tech.deplant.java4ever.framework.template to com.fasterxml.jackson.databind;
	opens tech.deplant.java4ever.framework to com.fasterxml.jackson.databind;
	opens tech.deplant.java4ever.framework.abi to com.fasterxml.jackson.databind;
	exports tech.deplant.java4ever.framework.abi.datatype;
	opens tech.deplant.java4ever.framework.abi.datatype to com.fasterxml.jackson.databind;
}