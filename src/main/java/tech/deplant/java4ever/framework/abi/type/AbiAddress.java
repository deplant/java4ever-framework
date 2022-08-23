package tech.deplant.java4ever.framework.abi.type;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import tech.deplant.java4ever.framework.type.Address;

import java.io.IOException;

public record AbiAddress(String addressString) implements AbiValue {

    public AbiAddress(Address address) {
        this(address.makeAddrStd());
    }

    public class AbiAddressSerializer extends JsonSerializer<AbiAddress> {

        @Override
        public void serialize(AbiAddress value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeString(value.addressString());
            gen.writeEndObject();
        }
    }

    public class AbiAddressDeserializer extends JsonDeserializer<AbiAddress> {
        @Override
        public AbiAddress deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            return new AbiAddress(p.getValueAsString());
        }
    }
}
