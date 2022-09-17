package tech.deplant.java4ever.framework.abi;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Abi;

import java.util.List;
import java.util.Map;

public record CachedAbi(IAbi origin, Map<String, Function> functions, List<String> headers) implements IAbi {

	private static Logger log = LoggerFactory.getLogger(CachedAbi.class);

	public CachedAbi(IAbi origin) {
		this(origin, origin.functions(), origin.headers());
	}

	public static CachedAbi ofResource(String resourceName) throws JsonProcessingException {
		return new CachedAbi(JsonAbi.ofResource(resourceName));
	}

	@Override
	public Map<String, Function> functions() {
		return this.functions;
	}

	@Override
	public List<String> headers() {
		return this.headers;
	}

	@Override
	public String json() {
		return this.origin.json();
	}

	@Override
	public boolean hasHeader(String name) {
		return this.origin.hasHeader(name);
	}

	@Override
	public boolean hasFunction(String name) {
		return this.origin.hasFunction(name);
	}

	@Override
	public boolean hasInput(String functionName, String inputName) {
		return this.origin.hasInput(functionName, inputName);
	}

	@Override
	public boolean hasOutput(String functionName, String outputName) {
		return this.origin.hasOutput(functionName, outputName);
	}

	@Override
	public String inputType(String functionName, String inputName) {
		return this.origin.inputType(functionName, inputName);
	}

	@Override
	public String outputType(String functionName, String outputName) {
		return this.origin.outputType(functionName, outputName);
	}

	@Override
	public Abi.ABI ABI() {
		return this.origin.ABI();
	}

	@Override
	public Map<String, Object> convertInputs(String functionName, Map<String, Object> functionInputs) {
		return this.origin.convertInputs(functionName, functionInputs);
	}

}
