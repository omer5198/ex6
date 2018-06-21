package oop.ex6.main;

/**
 * This class represents a variable. It can be either initialized with a value or not, has a valid name and
 * one of the specified sjava variable types
 */

public class Variable {

    private String type;

    private String name;

    private boolean initialized;

    private boolean isGlobal;

    /**
     * This is the constructor of the class. Initializes all parameters
     * @param type The type of the variable
     * @param name The name of the variable
     * @param isInitialized An indicator to whether the variable is initialized or not
     * @param isGlobal indicates whether the variable is global or not
     */
    public Variable(String type, String name, boolean isInitialized, boolean isGlobal) {
        this.type = type;
        this.name = name;
        this.initialized = initialized;
        this.isGlobal = isGlobal;

    }

    /**
     * a getter of initialized
     * @return whether is initialized or not
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * a getter of the type
     * @return the type of the variable
     */
    public String getType() {
        return type;
    }

    /**
     * a getter for the name of the variable
     * @return the name of the variable
     */
    public String getName() {
        return name;
    }

    /**
     * A getter for isGlobal
     * @return whether the variable is global or not
     */
    public boolean isGlobal() {
        return isGlobal;
    }
}
