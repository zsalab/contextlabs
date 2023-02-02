package contextlabs.model.attributes;

import java.util.Set;
import java.util.function.Function;

import contextlabs.model.query.Operators;

public final class EnumeratedAttribute extends Attribute<Enum<? extends AttributeEnum>> {

	private EnumeratedAttribute(int id, Enum<? extends AttributeEnum> value) {
		super(id, value);
	}	

	@Override
	protected Set<Operators> getSupportedOperators() {
		return SUPPORTED_OPERATORS_BASIC;
	}


	public static EnumeratedAttribute of(int id, String stringValue, Function<String,Enum<? extends AttributeEnum>> enumValueReader) {
		try {
			return new EnumeratedAttribute(id, enumValueReader.apply(stringValue.toUpperCase()));
		} catch (IllegalArgumentException | NullPointerException e) {
			return null;
		}
	}
}
