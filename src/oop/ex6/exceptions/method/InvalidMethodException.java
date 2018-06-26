package oop.ex6.exceptions.method;

/**
 * A general exception thrown upon an invalid method
 */
public class InvalidMethodException extends MethodException {
	public InvalidMethodException(String msg){
		super(msg);
	}
}
