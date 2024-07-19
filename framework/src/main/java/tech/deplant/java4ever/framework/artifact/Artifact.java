package tech.deplant.java4ever.framework.artifact;

import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Base interface for external artifacts for use with SDK (jsons, tvc byte files and so on).
 * Interface extends functional interfaces Supplier and Consumer.
 *
 * @param <S> the type parameter
 * @param <C> the type parameter
 */
public interface Artifact<S,C> extends Supplier<S>, Consumer<C> {
	/**
	 * Method checks what is the type of path for some string path
	 *
	 * @param artifactPath provided artifact path to check
	 * @return the path type enum
	 */
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

	/**
	 * The enum of different Path types to artifacts
	 */
	enum PathType {
		/**
		 * Resource path type.
		 */
		RESOURCE,
		/**
		 * Absolute path type.
		 */
		ABSOLUTE,
		/**
		 * Relational path type.
		 */
		RELATIONAL;
	}
}
