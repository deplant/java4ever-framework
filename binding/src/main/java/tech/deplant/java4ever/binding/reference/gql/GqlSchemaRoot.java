package tech.deplant.java4ever.binding.reference.gql;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import tech.deplant.java4ever.binding.generator.reference.*;

import java.util.Collection;

public record GqlSchemaRoot(GqlData data) {

	public enum GqlKind { SCALAR, ENUM, OBJECT, INTERFACE, INPUT_OBJECT }


	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
			include = JsonTypeInfo.As.EXISTING_PROPERTY,
			property = "kind",
			visible = true)
	@JsonSubTypes({
			@JsonSubTypes.Type(value = GqlType.GqlScalar.class, name = "SCALAR"),
			@JsonSubTypes.Type(value = GqlType.GqlObject.class, name = "OBJECT"),
			@JsonSubTypes.Type(value = GqlType.GqlEnum.class, name = "ENUM"),
			@JsonSubTypes.Type(value = GqlType.GqlInterface.class, name = "INTERFACE"),
			@JsonSubTypes.Type(value = GqlType.GqlInputObject.class, name = "INPUT_OBJECT")
	})
	public sealed interface GqlType {

		GqlKind kind();

		String name();

		String description();

		@JsonIgnoreProperties(ignoreUnknown = true)
		record GqlScalar(GqlKind kind, String name, String description) implements GqlType {}
		@JsonIgnoreProperties(ignoreUnknown = true)
		record GqlEnum(GqlKind kind, String name, String description, Collection<GqlEnumValue> enumValues) implements GqlType {}
		@JsonIgnoreProperties(ignoreUnknown = true)
		record GqlObject(GqlKind kind, String name, String description, Collection<GqlField> fields, Collection<GqlParam> interfaces) implements GqlType {}
		@JsonIgnoreProperties(ignoreUnknown = true)
		record GqlInterface(GqlKind kind, String name, String description, Collection<GqlParam> possibleTypes) implements GqlType {}
		@JsonIgnoreProperties(ignoreUnknown = true)
		record GqlInputObject(GqlKind kind, String name, String description, Collection<GqlInputField> inputFields) implements GqlType {}
	}

	public record GqlData(GqlSchema __schema) {
	}

	public record GqlSchema(GqlActionType queryType,
	                        GqlActionType mutationType,
	                        GqlActionType subscriptionType,
	                        Collection<GqlType> types,
	                        Collection<GqlDirective> directives) {
	}

	public record GqlField(String name, String description, Collection<GqlInputField> args, GqlParam type, boolean isDeprecated, String deprecationReason) {}

	public record GqlInputField(String name, String description, GqlParam type, String defaultValue) {}

	public record GqlEnumValue(String name, String description, boolean isDeprecated, String deprecationReason) {}

	public record GqlParam(String kind, Object type, String name, String description, GqlParam ofType) {
	}

	public record GqlActionType(String name) {
	}

	public record GqlDirective(String name, String description, Collection<GqlInputField> args, Collection<Object> locations) {
	}

	public record GqlDirectiveArg(String name, String description, String defaultValue, GqlParam type) {}


}
