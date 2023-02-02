package contextlabs.model.attributes;

import java.util.Set;

import contextlabs.model.query.Operators;

public final class IntegerAttribute extends Attribute<Integer> {
	
	private IntegerAttribute(int id, Integer value) {
		super(id, value);
	}

	@Override
	protected Set<Operators> getSupportedOperators() {
		return SUPPORTED_OPERATORS_ALL;
	}

	public static IntegerAttribute of(int id, String stringValue) {
		Integer intVal = Integer.valueOf(stringValue);
		if (intVal != null) {
			return new IntegerAttribute(id, intVal);
		}
		return null;
	}
}
