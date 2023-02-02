package contextlabs.model.attributes;

import java.util.Set;

import contextlabs.model.query.Operators;

public final class BooleanAttribute extends Attribute<Boolean> {

	private BooleanAttribute(int id, Boolean value) {
		super(id, value);
	}

	@Override
	protected Set<Operators> getSupportedOperators() {
		return SUPPORTED_OPERATORS_BASIC;
	}

	public static BooleanAttribute of(int id, String stringValue) {
		return new BooleanAttribute(id, Boolean.valueOf(stringValue));
	}
}
