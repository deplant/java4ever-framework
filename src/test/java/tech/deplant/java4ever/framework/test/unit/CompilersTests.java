package tech.deplant.java4ever.framework.test.unit;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class CompilersTests {

	private static Logger log = LoggerFactory.getLogger(CompilersTests.class);

	@Test
	public void get_envs() {
		log.debug(System.getenv("TON_CLIENT_LIB"));
		log.debug(System.getenv("PATH"));
		log.debug(System.getenv("TEMP"));
		log.debug(System.getenv("JAVA_HOME"));
		log.debug(System.getenv("LOCAL_NODE_ENPOINT"));
	}

}
