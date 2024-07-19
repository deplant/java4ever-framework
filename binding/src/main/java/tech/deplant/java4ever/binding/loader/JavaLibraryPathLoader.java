package tech.deplant.java4ever.binding.loader;

public record JavaLibraryPathLoader(String libraryName) implements LibraryLoader {

	public static final JavaLibraryPathLoader TON_CLIENT = new JavaLibraryPathLoader("ton_client");
	private final static System.Logger logger = System.getLogger(JavaLibraryPathLoader.class.getName());

	@Override
	public void load() {
		System.loadLibrary(this.libraryName);
		logger.log(System.Logger.Level.TRACE,
		           () -> "Library loaded: %s on path: %s".formatted(this.libraryName,
		                                                            System.getProperty("java.library.path")));
	}
}
