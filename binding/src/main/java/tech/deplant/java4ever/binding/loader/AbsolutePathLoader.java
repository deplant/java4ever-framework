package tech.deplant.java4ever.binding.loader;

import java.nio.file.Path;
import java.nio.file.Paths;

public record AbsolutePathLoader(Path filepath) implements LibraryLoader {

	private final static System.Logger logger = System.getLogger(AbsolutePathLoader.class.getName());

	public AbsolutePathLoader {
		if (!filepath.isAbsolute()) {
			throw new IllegalArgumentException(
					"Filepath of AbsolutePathLoader should be absolute. Filepath: " + filepath);
		}
	}

	public AbsolutePathLoader(String filePathString) {
		this(Paths.get(filePathString));
	}

	public static AbsolutePathLoader ofUserDir(String fileName) {
		return new AbsolutePathLoader(System.getProperty("user.dir"));
	}

	public static AbsolutePathLoader ofSystemEnv(String envName) {
		String path = System.getenv(envName);
		logger.log(System.Logger.Level.TRACE,
		           () -> "Path from ENV: %s".formatted(path));
		return new AbsolutePathLoader(path);
	}

	@Override
	public void load() {
		System.load(this.filepath.toString());
	}
}
