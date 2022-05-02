package tech.deplant.java4ever.framework.type;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.math.BigInteger;

public class UintBigIntegerAdapter implements JsonSerializer<BigInteger>, JsonDeserializer<BigInteger> {

    @Override
    public JsonElement serialize(BigInteger src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive("0x" + src.toString(16));
    }

    @Override
    public BigInteger deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        String str = json.getAsString();

        if (str.length() >= 2 && "0x".equals(str.substring(0, 2))) {
            str = str.substring(2);
            return new BigInteger(str, 16);
        } else {
            return new BigInteger(str, 10);
        }

    }

}