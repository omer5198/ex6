package oop.ex6.exceptions.parsing;

/**
 * This exception is thrown when a line is generally invalid (for instance, does not end with ";")
 */
public class InvalidLineException extends Exception {
	public InvalidLineException(String msg){
		super(msg);
	}
}
