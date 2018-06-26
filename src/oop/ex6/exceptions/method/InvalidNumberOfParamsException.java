package oop.ex6.exceptions.method;

/**
 * An exceptions when a method call has more parameters than the method should receive
 */
public class InvalidNumberOfParamsException extends ParameterException {
	public InvalidNumberOfParamsException(String msg){
		super(msg);
	}
}
