package oop.ex6.main;

import java.util.ArrayList;

public class Method {

    protected ArrayList<Tuple<String, Boolean>> parameterTypes; // arraylist and order matters
    // it holds a tuple of the boolean (final or not final) and type in string
    public Method(ArrayList<Tuple<String, Boolean>> parameters){
        this.parameterTypes = parameters;
    }

    public ArrayList<Tuple<String, Boolean>> getParameterTypes() {
        return parameterTypes;
    }
}
