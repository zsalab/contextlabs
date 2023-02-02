package contextlabs.components;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import contextlabs.model.attributes.Colors;
import contextlabs.model.attributes.ProductAttributes;
import contextlabs.model.query.Operators;
import contextlabs.model.query.Rule;

class SimpleRuleParserTest {

	@Test
	void test() {
		SimpleRuleParser ruleParser = new SimpleRuleParser();
		Rule rule = ruleParser.parseRule("color != BLUE && price < 17.75 && quantity > 750 && quantity < 5000 -> 100");
		Assertions.assertThat(rule.getScore()).isEqualTo(100);
		Assertions.assertThat(rule.getConditions())
			.containsOnlyKeys(ProductAttributes.COLOR, ProductAttributes.PRICE, ProductAttributes.QUANTITY);
		
		Assertions.assertThat(rule.getConditions().get(ProductAttributes.COLOR)).hasSize(1);
		Assertions.assertThat(rule.getConditions().get(ProductAttributes.COLOR).get(0).getOperator()).isEqualTo(Operators.NE);
		Assertions.assertThat(rule.getConditions().get(ProductAttributes.COLOR).get(0).getAttribute().getValue()).isEqualTo(Colors.BLUE);
		
		Assertions.assertThat(rule.getConditions().get(ProductAttributes.PRICE)).hasSize(1);
		Assertions.assertThat(rule.getConditions().get(ProductAttributes.PRICE).get(0).getOperator()).isEqualTo(Operators.LT);
		Assertions.assertThat(rule.getConditions().get(ProductAttributes.PRICE).get(0).getAttribute().getValue()).isEqualTo(new BigDecimal(17.75));
		
		Assertions.assertThat(rule.getConditions().get(ProductAttributes.QUANTITY)).hasSize(2);
	}
}
