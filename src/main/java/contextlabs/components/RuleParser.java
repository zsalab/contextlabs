package contextlabs.components;

import contextlabs.model.query.Rule;

public interface RuleParser {
	// only && supported
	final static String CONDITION_SEPARATOR = "&&";
	final static String SCORE_SEPARATOR = "->";

	Rule parseRule(String strRule);
}
