package contextlabs.model.attributes;

import java.math.BigDecimal;
import java.util.Set;

import contextlabs.model.query.Operators;

public final class NumberAttribute extends Attribute<BigDecimal> {

	private NumberAttribute(int id, BigDecimal value) {
		super(id, value);
	}

	@Override
	protected Set<Operators> getSupportedOperators() {
		return SUPPORTED_OPERATORS_ALL;
	}

	public static NumberAttribute of(int id, String stringValue) {
		try {
			return new NumberAttribute(id, new BigDecimal(stringValue));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
