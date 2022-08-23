package tech.deplant.java4ever.framework.abi.type;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;

public record AbiUint(BigInteger bigInteger) {

    public AbiUint(Long longVal) {
        this(BigInteger.valueOf(longVal));
    }

    public AbiUint(Instant instant) {
        this(instant.getEpochSecond());
    }

    public class AbiUintSerializer extends JsonSerializer<AbiUint> {

        @Override
        public void serialize(AbiUint value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeString("0x" + bigInteger().toString(16));
            gen.writeEndObject();
        }
    }

    public class AbiUintDeserializer extends JsonDeserializer<AbiUint> {
        @Override
        public AbiUint deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            var str = p.getValueAsString();
            if (str.length() >= 2 && "0x".equals(str.substring(0, 2))) {
                str = str.substring(2);
                return new AbiUint(new BigInteger(str, 16));
            } else {
                return new AbiUint(new BigInteger(str, 10));
            }
        }
    }
}
