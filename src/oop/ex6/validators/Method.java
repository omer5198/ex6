package oop.ex6.validators;

/**
 * This class represents a method in the javas file. It helps us checking for validity of a method call, for
 * example.
 */

import java.util.ArrayList;

public class Method {

    private ArrayList<String> parameterTypes; // arraylist and order matters

    private String name;
    // holds the types of the variables
    public Method(String name, ArrayList<String> parameters){
        this.name = name;
        this.parameterTypes = parameters;
    }

    /**
     * This method returns the list of parameter types of the method.
     * @return A list with strings representing the input parameters type of the method.
     */
    public ArrayList<String> getParameterTypes() {
        return parameterTypes;
    }

}
