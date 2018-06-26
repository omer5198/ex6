package oop.ex6.exceptions.variable;

/**
 * This exception regards assigning illegal value to a variable
 */
public class InvalidValueException extends VariableException {
	public InvalidValueException(String msg){
		super(msg);
	}
}
