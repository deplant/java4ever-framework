package tech.deplant.java4ever.frtest.unit;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class CompilersTests {

	private static System.Logger logger = System.getLogger(CompilersTests.class.getName());

	@Test
	public void get_envs() {
		logger.log(System.Logger.Level.DEBUG, () -> System.getenv("TON_CLIENT_LIB"));
		logger.log(System.Logger.Level.DEBUG, () -> System.getenv("PATH"));
		logger.log(System.Logger.Level.DEBUG, () -> System.getenv("TEMP"));
		logger.log(System.Logger.Level.DEBUG, () -> System.getenv("JAVA_HOME"));
		logger.log(System.Logger.Level.DEBUG, () -> System.getenv("LOCAL_NODE_ENPOINT"));
	}

}
