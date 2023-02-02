package contextlabs.model.attributes;

import java.io.Serializable;
import java.util.function.Function;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductAttributes {
	NAME(strVal -> StringAttribute.of(0, strVal)),
	TYPE(strVal -> StringAttribute.of(0, strVal)),
	COLOR(strVal -> EnumeratedAttribute.of(0, strVal, Colors::valueOf)),
	PRICE(strVal -> NumberAttribute.of(0, strVal)),
	QUANTITY(strVal -> IntegerAttribute.of(0, strVal)),
	WEIGTH(strVal -> NumberAttribute.of(0, strVal)),
	AVAILABLE(strVal -> BooleanAttribute.of(0, strVal));
	
	final Function<String,Attribute<? extends Serializable>> valueDeserializer;
	
	public Attribute<? extends Serializable> getValue(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return this.valueDeserializer.apply(value.trim());
	}

	public static ProductAttributes fromString(String name) {
		try {
			return ProductAttributes.valueOf(name);
		} catch (IllegalArgumentException | NullPointerException e) {
			return null;
		}
	}
}
