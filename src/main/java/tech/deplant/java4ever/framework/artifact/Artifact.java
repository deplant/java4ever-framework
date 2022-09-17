package tech.deplant.java4ever.framework.artifact;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface Artifact<T> {
	static Path resourceToPath(String resourcePath) {
		var cleanedPath = resourcePath.replace(File.separator, "/");
		if (cleanedPath.length() > 0 && !"/".equals(cleanedPath.substring(0, 1))) {
			cleanedPath = "/" + cleanedPath;
		}
		// workaround for problem that Resource addresses should start with .
		if (cleanedPath.length() > 0 && !".".equals(cleanedPath.substring(0, 1))) {
			cleanedPath = "." + cleanedPath;
		}
		//log.trace("Accessing file:" + cleanedPath);

		try {
			return Paths.get(
					Artifact.class.getClassLoader().getResource(cleanedPath).toURI()
			);
		} catch (URISyntaxException | NullPointerException e) {
			//log.error("Wrong path: Path: " + cleanedPath + ", Error: " + e.getMessage());
			return null;
		}
	}

	static Path ofAbsolutePath(String absolutePath) {
		var cleanedPath = absolutePath.replace(File.separator, "/");
		return Paths.get(cleanedPath);
	}

	void write(T content) throws IOException;

	T read() throws IOException;
}
