package tech.deplant.java4ever.framework.abi;

import tech.deplant.java4ever.binding.Abi;

import java.util.List;
import java.util.Map;

public interface IAbi {

	Map<String, Function> functions();

	List<String> headers();

	String json();

	boolean hasHeader(String name);

	boolean hasFunction(String name);

	boolean hasInput(String functionName, String inputName);

	boolean hasOutput(String functionName, String outputName);

	String inputType(String functionName, String inputName);

	String outputType(String functionName, String outputName);

	Abi.ABI ABI();

	Map<String, Object> convertInputs(String functionName, Map<String, Object> functionInputs);

	record Function(Map<String, String> inputs, Map<String, String> outputs) {
	}

}

