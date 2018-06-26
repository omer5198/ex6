package oop.ex6.exceptions.method;

/**
 * This exception is thrown when a method is declared in an inner scope (which is illegal)
 */
public class MethodInInnerScopeException extends MethodException {
    public MethodInInnerScopeException(String message){
        super(message);
    }
}
