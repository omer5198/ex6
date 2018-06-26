package oop.ex6.exceptions.parsing;

/**
 * This exception is thrown upon an invalid curly brackets use in the file
 */
public class InvalidBracketsException extends Exception {
    public InvalidBracketsException(String message){
        super(message);
    }

}
