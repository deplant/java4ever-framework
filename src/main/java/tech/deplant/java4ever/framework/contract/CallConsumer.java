package tech.deplant.java4ever.framework.contract;

import java.util.Objects;

@FunctionalInterface
public interface CallConsumer<NAME, INPUTS, HEADER, KEYS> {

	void accept(NAME name, INPUTS inputs, HEADER header, KEYS keys);

	default CallConsumer<NAME, INPUTS, HEADER, KEYS> andThen(CallConsumer<? super NAME, ? super INPUTS, ? super HEADER, ? super KEYS> after) {
		Objects.requireNonNull(after);

		return (name, inputs, header, keys) -> {
			accept(name, inputs, header, keys);
			after.accept(name, inputs, header, keys);
		};
	}
}
