package tech.deplant.java4ever.binding.loader;

/**
 * Every EVER-SDK loader should implement this interface to be used in EverSdk.load() statement.
 */
public interface LibraryLoader {
    static LibraryLoader ofType(LibraryLoaderType type, String value) {
		return switch (type) {
            case ENV -> AbsolutePathLoader.ofSystemEnv(value);
            case PATH -> new AbsolutePathLoader(value);
            case JAVALIB -> new JavaLibraryPathLoader(value);
            case DEFAULT -> new DefaultLoader(Thread.currentThread().getContextClassLoader());
		};
	}

	/**
	 * Any implementation should contain System.load(...); System.loadLibrary(...)
	 * to correctly load native library connection.
	 */
	void load();
}
