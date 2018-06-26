package oop.ex6.exceptions.variable;

/**
 * This exception regards using variables that are not initialized
 */
public class UninitializedException extends VariableException {
	public UninitializedException(String msg){
		super(msg);
	}
}
