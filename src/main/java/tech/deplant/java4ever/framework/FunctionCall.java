package tech.deplant.java4ever.framework;

import lombok.Value;

import java.util.Map;

@Value
public class FunctionCall {
    String name;
    Map<String, Object> inputs;
}
