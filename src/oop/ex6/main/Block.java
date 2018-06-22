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

    private ArrayList<String> codeLines;

    private boolean isMethod;

    /**
     * The constructor of the block. It receives a hashmap of the variables in it's scope (and in outer
     * scopes)
     * @param blockVariables The hashmap of variables
     * @param parent the parent block of the current block
	 * @param isMethod whether or not the block is a method
     */
    Block(HashMap<String, Variable> blockVariables, Block parent, ArrayList<String> codeLines,
		  boolean isMethod){
        this.blockVariables = blockVariables;
        this.parent = parent;
        this.codeLines = codeLines;
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
     * getter for the code lines
     * @return the code lines of the block
     */
    public ArrayList<String> getCodeLines() {
        return codeLines;
    }

    public void addLine(String line){
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
}
