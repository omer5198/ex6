package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a block that a parser receives. This helps us deal with scopes of variables for
 * example when parsing through our file.
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

    public void addLine(Tuple<String, Integer> line){
        codeLines.add(line);
    }

    //TODO implement
    public void addDeclaredVariables(String line){

    }

    public Block getParent() {
        return parent;
    }

	public boolean isMethod() {
    	return isMethod;
	}

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

	public Variable getLocalVariable(String name) {
    	return blockVariables.get(name);
	}
}
