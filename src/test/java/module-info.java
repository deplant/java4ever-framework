module test {
	requires java4ever.framework;
	requires java4ever.binding;
	requires org.junit.jupiter.api;
	requires java.compiler;
	opens tech.deplant.java4ever.framework.unit to org.junit.platform.commons;
	exports tech.deplant.java4ever.framework.unit;
	exports tech.deplant.java4ever.framework.codegen;

}