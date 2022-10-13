module java4ever.framework {
	requires org.slf4j;
	requires transitive java4ever.binding;
	requires transitive com.fasterxml.jackson.databind;
	requires transitive com.fasterxml.jackson.module.paramnames;
	requires transitive com.fasterxml.jackson.datatype.jdk8;
	requires transitive com.fasterxml.jackson.datatype.jsr310;
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
}