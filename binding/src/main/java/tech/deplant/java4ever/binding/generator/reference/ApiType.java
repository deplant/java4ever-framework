package tech.deplant.java4ever.binding.generator.reference;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.EXISTING_PROPERTY,
		property = "type",
		visible = true)
@JsonSubTypes({
		@JsonSubTypes.Type(value = StructType.class, name = "Struct"),
		@JsonSubTypes.Type(value = EnumOfConsts.class, name = "EnumOfConsts"),
		@JsonSubTypes.Type(value = EnumOfTypes.class, name = "EnumOfTypes"),
		@JsonSubTypes.Type(value = OptionalType.class, name = "Optional"),
		@JsonSubTypes.Type(value = GenericType.class, name = "Generic"),
		@JsonSubTypes.Type(value = RefType.class, name = "Ref"),
		@JsonSubTypes.Type(value = ArrayType.class, name = "Array"),
		@JsonSubTypes.Type(value = NumberType.class, name = "Number"),
		@JsonSubTypes.Type(value = StringType.class, name = "String"),
		@JsonSubTypes.Type(value = BooleanType.class, name = "Boolean"),
		@JsonSubTypes.Type(value = BigIntType.class, name = "BigInt"),
		@JsonSubTypes.Type(value = NoneType.class, name = "None")
})
public sealed interface ApiType permits ArrayType, BigIntType, BooleanType, EnumOfConsts, EnumOfTypes, GenericType, NoneType, NumberType, OptionalType, RefType, StringType, StructType {

	String name();

	String type();

	String summary();

	String description();

}
