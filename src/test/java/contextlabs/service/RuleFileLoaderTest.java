package contextlabs.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import contextlabs.model.query.Rule;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class RuleFileLoaderTest {
	@Autowired
	protected RuleFileLoader ruleFileLoader;

	@Test
	void test() {
		List<Rule> rules = ruleFileLoader.getRules();
		Assertions.assertThat(rules).hasSize(3);
	}

}
