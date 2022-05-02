package tech.deplant.java4ever.framework.type;

import com.google.gson.*;
import tech.deplant.java4ever.framework.Data;

import java.lang.reflect.Type;

public class AbiStringAdapter implements JsonSerializer<String>, JsonDeserializer<String> {

    @Override
    public JsonElement serialize(String src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(Data.strToHex(src));
    }

    @Override
    public String deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        return Data.hexToStr(json.getAsString());
    }
}