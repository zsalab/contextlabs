package contextlabs.service;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import contextlabs.model.ScoreResult;
import contextlabs.model.attributes.ProductAttributes;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ScoringServiceTest {
	@Autowired
	protected ScoringService scoringService;
	
	@Test
	void test() {
		ScoreResult scoreResult = scoringService.scoreProducts();
		Assertions.assertThat(scoreResult.getAveragePrice()).isEqualTo(new BigDecimal("15.7"));
		Assertions.assertThat(scoreResult.getTotalPrice()).isEqualTo(new BigDecimal("15.7"));
		Assertions.assertThat(scoreResult.getProductScores()).hasSize(1);
		Assertions.assertThat(scoreResult.getProductScores()).anySatisfy((product, score) -> {
			Assertions.assertThat(product.getAttributes().get(ProductAttributes.NAME).getValue()).isEqualTo("Prod55555");
			Assertions.assertThat(score).isEqualTo(80);
		});
	}

}
