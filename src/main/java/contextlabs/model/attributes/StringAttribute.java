package contextlabs.model.attributes;

import java.util.Set;

import contextlabs.model.query.Operators;

public final class StringAttribute extends Attribute<String> {
	
	private StringAttribute(int id, String value) {
		super(id, value);
	}

	@Override
	protected Set<Operators> getSupportedOperators() {
		return SUPPORTED_OPERATORS_BASIC;
	}

	public static StringAttribute of(int id, String stringValue) {
		return new StringAttribute(id, stringValue);
	}
}
