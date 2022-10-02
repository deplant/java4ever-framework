module java4ever.framework {
	requires java.scripting;
	requires org.slf4j;
	requires java4ever.binding;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.module.paramnames;
	requires com.fasterxml.jackson.datatype.jdk8;
	requires com.fasterxml.jackson.datatype.jsr310;
	exports tech.deplant.java4ever.framework;
	exports tech.deplant.java4ever.framework.artifact;
	exports tech.deplant.java4ever.framework.contract;
	exports tech.deplant.java4ever.framework.template;
	exports tech.deplant.java4ever.framework.type;
	exports tech.deplant.java4ever.framework.crypto;
	exports tech.deplant.java4ever.framework.template.type;
	opens tech.deplant.java4ever.framework.contract to com.fasterxml.jackson.databind;
	opens tech.deplant.java4ever.framework.template to com.fasterxml.jackson.databind;
}