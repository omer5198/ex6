package oop.ex6.exceptions.method;

/**
 * An exception thrown when a method receives invalid parameters
 */
public class InvalidParameterException extends ParameterException {
	public InvalidParameterException(String msg){
		super(msg);
	}
}
