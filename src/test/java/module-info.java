module test {
	requires java4ever.binding;
	requires java4ever.framework;
	requires org.slf4j;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.datatype.jdk8;
	requires com.fasterxml.jackson.datatype.jsr310;
	requires com.fasterxml.jackson.module.paramnames;
	requires org.junit.jupiter.api;
	exports tech.deplant.java4ever.framework.test.unit;
}