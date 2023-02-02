package contextlabs.model.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Operators {
	// Order matters (for example <= must come before the <)
	EQ("=="), NE("!="), LE("<="), LT("<"), GE(">="), GT(">");
	
	final String strOperator;
}
