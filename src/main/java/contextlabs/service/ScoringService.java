package contextlabs.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;

import contextlabs.model.Product;
import contextlabs.model.ScoreResult;
import contextlabs.model.attributes.Attribute;
import contextlabs.model.attributes.ProductAttributes;
import contextlabs.model.query.Condition;
import contextlabs.model.query.Rule;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;

@Service
public class ScoringService {
	private static final Float ZERO_SCORE = Float.valueOf(0);
	@Autowired
	private RuleLoader ruleLoader;
	@Autowired
	private ProductLoader productLoader;
	
	@Value(value = "${product-score-threshold}")
	private Float productScoreThreshold;

	@Value(value = "${single-rule-score-threshold-precent}")
	private Float singleRuleScoreThreshold;
	
	@PostConstruct
	public void init() {
		if (this.singleRuleScoreThreshold > 1) {
			this.singleRuleScoreThreshold /= 100;
		}
	}
	
	public ScoreResult scoreProducts() {
		List<Rule> rules = ruleLoader.getRules();
		AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.ZERO);
		ImmutableMap<Product, Float> productScores = ImmutableMap.copyOf(productLoader.getProducts()
			.flatMap(product -> Flux.just(Pair.of(product, calculateScore(product, rules))))
			.filter(scoredProd -> scoredProd.getValue() >= this.productScoreThreshold)
			.doOnNext(scoredProd -> totalPrice.getAndUpdate(sum -> sum.add(
					(BigDecimal)scoredProd.getKey().getAttributes().get(ProductAttributes.PRICE).getValue())))
			.collectMap(Pair::getKey, Pair::getValue)
			.block());
		return new ScoreResult(
				productScores, 
				productScores.size() >  0 ? totalPrice.get().divide(BigDecimal.valueOf(productScores.size()), RoundingMode.HALF_UP) : BigDecimal.ZERO, 
				totalPrice.get());
	}
	
	private Float calculateScore(Product product, List<Rule> rules) {
		float totalScore = ZERO_SCORE;
		for (Rule rule: rules) {
			int totalConditionCount = 0;
			int matchedConditionCount = 0;
			for (ProductAttributes condAttr: rule.getConditions().keySet()) {
				Attribute<? extends Serializable> prodAttr = product.getAttributes().get(condAttr);
				if (prodAttr != null) {
					for (Condition condition: rule.getConditions().get(condAttr)) {
						totalConditionCount++;
						if (prodAttr.evaluate(condition.getAttribute(), condition.getOperator())) {
							matchedConditionCount++;
						}
					}
				} else {
					totalConditionCount += rule.getConditions().get(condAttr).size();
				}
			}
			if (matchedConditionCount > 0) {
				float scorePrecent = (float)matchedConditionCount / totalConditionCount;
				if (this.singleRuleScoreThreshold < scorePrecent) {
					totalScore += (float)rule.getScore() * scorePrecent;
				}
			}
		}
		return totalScore;
	}
}
