package oop.ex6.main.parsing;

import oop.ex6.tools.Tuple;
import oop.ex6.validators.Variable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a block (scope) that a parser receives.
 * This helps us deal with scopes of variables,
 * for example when parsing through our file. Using this we can easily deal with local variables, etc.
 */
public class Block {

    private Block parent;

    private HashMap<String, Variable> blockVariables;

    private ArrayList<Tuple<String, Integer>> codeLines;

    private Boolean isMethod;

    /**
     * The constructor of the block. It receives a hashmap of the variables in it's scope (and in outer
     * scopes)
     * @param parent the parent block of the current block
	 * @param isMethod whether the block is method or not
     */
    Block(Block parent, Boolean isMethod){
        this.blockVariables = new HashMap<String, Variable>();
        this.parent = parent;
		this.codeLines = new ArrayList<Tuple<String, Integer>>();
        this.isMethod = isMethod;
    }

    /**
     * A getter for the variables
     * @return the variables of the block
     */
    public HashMap<String, Variable> getVariablesInBlock() {
        return blockVariables;
    }

	/**
	 * Adds a variable to the blockVariables
	 * @param var the variable to add
	 */
	public void addVariable(Variable var) {
    	blockVariables.put(var.getName(), var);
	}

    /**
     * getter for the code lines
     * @return the code lines of the block
     */
    public ArrayList<Tuple<String, Integer>> getCodeLines() {
        return codeLines;
    }

	/**
	 * Adds a line to the list of lines in the block.
	 * @param line A tuple containing the text of the line and the number of the line in the code file.
	 */
	public void addLine(Tuple<String, Integer> line){
        codeLines.add(line);
    }


	/**
	 * A getter for the parent block of the block
	 * @return the parent of the block
	 */
	public Block getParent() {
        return parent;
    }

	/**
	 * Returns whether the block is a method defining block or not
	 * @return the isMethod indicator
	 */
	public boolean isMethod() {
    	return isMethod;
	}

	/**
	 * returns a variable object of a given name. If not in the current block,
	 * it will go up to outer blocks (ancestors) and check if it is there.
	 * @param name The name of the variable we search for, i.e check if it is
	 *                defined somewhere in the block
	 * @return the variable object with the given name if it is defined in the block, null otherwise.
	 */
	public Variable getVariable(String name) {
    	Block current = this;
    	Variable variable = null;
    	while(current != null && variable == null) {
    		HashMap<String, Variable> blockVariables = current.getVariablesInBlock();
    		variable = blockVariables.get(name);
    		current = current.getParent();
		}
		return variable;
	}

	/**
	 * This method checks if there is a variable of a given name inside this
	 * specific block (local variable).
	 * @param name The given name of the variable
	 * @return Either the variable object with the given name, null if there is no
	 * such variable in the block.
	 */
	public Variable getLocalVariable(String name) {
    	return blockVariables.get(name);
	}
}
