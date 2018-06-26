package oop.ex6.exceptions.variable;

/**
 * This exception regards invalid types for parameters
 */
public class InvalidTypeException extends VariableException {
	public InvalidTypeException(String msg){
		super(msg);
	}
}
