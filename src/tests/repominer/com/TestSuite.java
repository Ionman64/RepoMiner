package tests.repominer.com;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.repominer.com.Repository;

class TestSuite {

	@Test
	void cloneProject() {
		Repository testRepo = new Repository();
		assert(testRepo.cloneProject());
	}

}
