package oop.ex6.exceptions.variable;

/**
 * This exception is thrown upon an error regarding the final keyword (i.e changing a final variable)
 */
public class FinalException extends VariableException {
	public FinalException(String msg){
		super(msg);
	}
}
