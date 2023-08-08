module test {
	requires java4ever.framework;
	requires java4ever.binding;
	requires org.junit.jupiter.api;
	requires java.compiler;
	requires java.net.http;
	opens tech.deplant.java4ever.frtest.unit to org.junit.platform.commons;
	opens tech.deplant.java4ever.frtest.unit.datatype to org.junit.platform.commons;
	exports tech.deplant.java4ever.frtest.unit;
	exports tech.deplant.java4ever.frtest.codegen;
	exports tech.deplant.java4ever.frtest;
	exports tech.deplant.java4ever.frtest.unit.datatype;

}