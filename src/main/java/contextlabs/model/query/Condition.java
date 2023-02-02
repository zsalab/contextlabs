package contextlabs.model.query;

import java.io.Serializable;

import contextlabs.model.attributes.Attribute;
import lombok.Value;

@Value
public final class Condition {
	private final Attribute<? extends Serializable> attribute; 
	private final Operators operator;
}
