package oop.ex6.main;

/**
 * This class represents a variable. It can be either initialized with a value or not, has a valid name and
 * one of the specified sjava variable types
 */

public class Variable {

    public static int UNINITIALIZED = -1;

    private String type;

    private String name;

    private int initializationLine;

    private boolean isGlobal;

    private boolean isFinal;

    /**
     * This is the constructor of the class. Initializes all parameters
     * @param type The type of the variable
     * @param name The name of the variable
     * @param initializationLine The line of which it was created
     * @param isGlobal indicates whether the variable is global or not
	 * @param isFinal indicates whether the variable is final or not
     */
    public Variable(String type, String name, int initializationLine, boolean isGlobal, boolean isFinal) {
        this.type = type;
        this.name = name;
        this.initializationLine = UNINITIALIZED;
        this.isGlobal = isGlobal;
        this.isFinal = isFinal;
    }

    /**
     * checks if the variable is initialized
     * @return whether is initialized or not
     */
    public boolean isInitialized() {
        return UNINITIALIZED != -1;
    }

    /**
     * getter for the initialization line
     * @return line of the variable's initialization (-1 if uninitialized)
     */
    public int getLine() {
        return initializationLine;
    }

    /**
     * a getter of the type
     * @return the type of the variable
     */
    public String getType() {
        return type;
    }

	/**
	 * @return Returns whether or not the variable is global
	 */
	public boolean isGlobal() {
		return isGlobal;
	}

	/**
	 * @return Returns whether or not the variable is final
	 */
	public boolean isFinal() {
		return isFinal;
	}

	/**
	 * @return the name of the variable
	 */
	public String getName() {
		return name;
	}
}
