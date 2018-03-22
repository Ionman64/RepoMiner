package tests.repominer.com;

import org.junit.jupiter.api.Test;
import org.repominer.com.GitHubRepo;

class TestSuite {

	@Test
	void cloneProject() {
		GitHubRepo testRepo = new GitHubRepo("");
		assert(testRepo.cloneProject());
	}

}
