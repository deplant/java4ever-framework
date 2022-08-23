package tech.deplant.java4ever.framework.abi.type;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import tech.deplant.java4ever.framework.Data;

import java.io.IOException;

@JsonSerialize(using = AbiString.AbiStringSerializer.class)
@JsonDeserialize(using = AbiString.AbiStringDeserializer.class)
public record AbiString(String text) {

    public class AbiStringSerializer extends JsonSerializer<AbiString> {

        @Override
        public void serialize(AbiString value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeString(Data.strToHex(value.text()));
            gen.writeEndObject();
        }
    }

    public class AbiStringDeserializer extends JsonDeserializer<AbiString> {
        @Override
        public AbiString deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            return new AbiString(Data.hexToStr(p.getValueAsString()));
        }
    }
}
