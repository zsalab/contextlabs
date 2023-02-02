package contextlabs.model.attributes;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import contextlabs.model.query.Operators;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
abstract public sealed class Attribute<T extends Serializable> permits BooleanAttribute, EnumeratedAttribute, IntegerAttribute, NumberAttribute, StringAttribute {
	protected static Set<Operators> SUPPORTED_OPERATORS_BASIC = Set.of(Operators.EQ, Operators.NE);
	protected static Set<Operators> SUPPORTED_OPERATORS_ALL = Set.of(Operators.values());

	protected final int id;
	protected final T value;

	public final boolean isOperatorSupported(Operators operator) {
		return getSupportedOperators().contains(operator);
	}

	abstract protected Set<Operators> getSupportedOperators();
	
	@SuppressWarnings("unchecked")
	public boolean evaluate(Attribute<? extends Serializable> other, Operators operator) {
		T left = this.getValue();
		if (this.getClass().isInstance(other)) {
			T right = (T)other.getValue();
			return evaluate(left,  right, operator);
		}
		throw new IllegalArgumentException("Incompatible argument types");
	}
	
	@SuppressWarnings("unchecked")
	private boolean evaluate(T left, T right, Operators operator) {
		if (isOperatorSupported(operator)) {
			switch (operator) {
				case EQ:
					return Objects.equals(left, right);
				case NE:
					return !Objects.equals(left, right);
				default:
					if ((left instanceof Comparable<?>) && (right instanceof Comparable<?>)) {
						switch (operator) {
							case GE:
								return ((Comparable<T>)left).compareTo(right) >= 0;
							case GT:
								return ((Comparable<T>)left).compareTo(right) > 0;
							case LE:
								return ((Comparable<T>)left).compareTo(right) <= 0;
							case LT:
								return ((Comparable<T>)left).compareTo(right) < 0;
							default:
								/* NOP */
						}					
					}
			}
		}
		throw new IllegalArgumentException("Not supported operator "+operator.getStrOperator());
	}
}