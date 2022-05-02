package tech.deplant.java4ever.framework.type;

import com.google.gson.*;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.framework.Address;

import java.lang.reflect.Type;
import java.math.BigInteger;

@Log4j2
public class AbiAddressAdapter implements JsonSerializer<Address>, JsonDeserializer<Address> {

    @Override
    public JsonElement serialize(Address src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(src.wid() + ":" + src.value().toString(16));
    }

    @Override
    public Address deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        log.debug(json);
        var splitted = json.getAsString().split(":");
        return new Address(Integer.parseInt(splitted[0]), new BigInteger(splitted[1], 16));
    }
}