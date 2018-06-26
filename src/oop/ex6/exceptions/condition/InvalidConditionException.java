package oop.ex6.exceptions.condition;

/**
 * An exception thrown upon an invalid condition (if\while)
 */
public class InvalidConditionException extends ConditionException {
    public InvalidConditionException(String msg){
        super(msg);
    }
}
