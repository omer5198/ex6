package oop.ex6.exceptions.method;

/**
 * An exception thrown when a method is called in the outer scope
 */
public class MethodCallInOuterScopeException extends MethodException {
    public MethodCallInOuterScopeException(String msg){
        super(msg);
    }
}
