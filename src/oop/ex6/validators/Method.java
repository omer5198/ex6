package oop.ex6.validators;

import java.util.ArrayList;

public class Method {

    private ArrayList<String> parameterTypes; // arraylist and order matters

    private String name;
    // holds the types of the variables
    public Method(String name, ArrayList<String> parameters){
        this.name = name;
        this.parameterTypes = parameters;
    }

    public ArrayList<String> getParameterTypes() {
        return parameterTypes;
    }

    public String getName() {
        return name;
    }
}
