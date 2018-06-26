package oop.ex6.exceptions.condition;

/**
 * An exception thrown upon an if\while statement defined in the outer scope
 */
public class ConditionInOuterScopeError extends ConditionException {
	public ConditionInOuterScopeError(String msg){
		super(msg);
	}
}
