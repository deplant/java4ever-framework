package tech.deplant.java4ever.framework.artifact;

import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Artifact<S,C> extends Supplier<S>, Consumer<C> {
	static PathType pathType(String artifactPath) {
		PathType pathType;
		if (Thread.currentThread().getContextClassLoader().getResourceAsStream(artifactPath) == null) {
			var path = Paths.get(artifactPath);
			if (path.isAbsolute()) {
				pathType = PathType.ABSOLUTE;
			} else {
				pathType = PathType.RELATIONAL;
			}
		} else {
			pathType = PathType.RESOURCE;
		}
		return pathType;
	}

	enum PathType {
		RESOURCE,
		ABSOLUTE,
		RELATIONAL;
	}
}
