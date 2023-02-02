package contextlabs.model.query;

import java.util.List;

import com.google.common.collect.ImmutableMap;

import contextlabs.model.attributes.ProductAttributes;
import lombok.Value;

@Value
public class Rule {
	private final ImmutableMap<ProductAttributes, List<Condition>> conditions;
	private final int score;
}
