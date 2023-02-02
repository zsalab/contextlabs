package contextlabs.model;

import java.math.BigDecimal;

import com.google.common.collect.ImmutableMap;

import lombok.Value;

@Value
public class ScoreResult {
	private final ImmutableMap<Product, Float> productScores;
	private final BigDecimal averagePrice;
	private final BigDecimal totalPrice;
}
