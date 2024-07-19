module java4ever.binding {
	requires java.compiler;
	requires transitive com.fasterxml.jackson.databind;
	requires transitive deplant.commons;
	requires transitive deplant.javapoet;
	requires com.fasterxml.jackson.datatype.jdk8;
	requires com.fasterxml.jackson.datatype.jsr310;
	requires com.fasterxml.jackson.module.paramnames;
	requires java.net.http;

	opens sdk.linux_x86_64;
	opens sdk.macos_aarch64;
	opens sdk.macos_x86_64;
	opens sdk.win_x86_64;

	exports tech.deplant.java4ever.binding;
	exports tech.deplant.java4ever.binding.gql;
	exports tech.deplant.java4ever.binding.loader;
	exports tech.deplant.java4ever.binding.io;
	exports tech.deplant.java4ever.binding.generator;
	exports tech.deplant.java4ever.binding.generator.reference;
	exports tech.deplant.java4ever.binding.generator.jtype;
}