package oop.ex6.exceptions.method;

/**
 * This is an exception thrown when something is wrong with the parameters a method receives
 */
public class ParameterException extends MethodException {
	public ParameterException(String msg){
		super(msg);
	}
}
