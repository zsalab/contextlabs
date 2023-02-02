package contextlabs.components;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

import contextlabs.model.attributes.Attribute;
import contextlabs.model.attributes.ProductAttributes;
import contextlabs.model.query.Condition;
import contextlabs.model.query.Operators;
import contextlabs.model.query.Rule;

@Component
public final class SimpleRuleParser implements RuleParser {
	
	@Value(value = "${rules-file}")
	private String fileName;
	
	public Rule parseRule(String strRule) {
		int scoreSepIdx = strRule.lastIndexOf(SCORE_SEPARATOR);
		if (scoreSepIdx < 0) {
			throw new RuntimeException("Invlid rule, No score separator. Rule: "+strRule);
		}
		Integer score  = Integer.valueOf(strRule.substring(scoreSepIdx+2).trim());
		if (score == null) {
			throw new RuntimeException("Invlid rule, Score not an integer. Rule: "+strRule);
		}
		Map<ProductAttributes, List<Condition>> conditions = new HashMap<>(ProductAttributes.values().length);
		for (String strCondition: strRule.substring(0, scoreSepIdx).split(CONDITION_SEPARATOR)) {
			boolean opFound = false;
			for (Operators operator: Operators.values()) {
				int opIdx = strCondition.indexOf(operator.getStrOperator());
				if (opIdx >= 0) {
					opFound = true;
					String[] tmp = strCondition.split(operator.getStrOperator());
					if (tmp.length != 2) {
						throw new RuntimeException("Invlid rule, Invalid condition (supported operators works only with two operands): "+strCondition);
					}
					ProductAttributes attribute = ProductAttributes.fromString(tmp[0].trim().toUpperCase());
					Attribute<? extends Serializable> value;
					if (attribute != null) {
						value = attribute.getValue(tmp[1].trim());
					} else {
						attribute = ProductAttributes.fromString(tmp[1].trim().toUpperCase());
						if (attribute != null) {
							value = attribute.getValue(tmp[0].trim());
						} else {
							throw new RuntimeException("Invalid rule, Unsupported attribute in the condition: "+strCondition);
						}
					}
					conditions.computeIfAbsent(attribute, a -> new LinkedList<>())
						.add(new Condition(value, operator));
					break;
				}
			}
			if (!opFound) {
				throw new RuntimeException("Invlid rule, No (supported) operator found in the condition: "+strCondition);
			}
		}
		if (conditions.isEmpty()) {
			return null;
		}
		return new Rule(ImmutableMap.copyOf(conditions), score);
	}
}
