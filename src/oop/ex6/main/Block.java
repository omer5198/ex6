package oop.ex6.main;

/**
 * This class represents a block that a parser receives. This helps us deal with scopes of variables for
 * example when parsing through our file.
 */

import java.util.HashMap;

public class Block {

    private Block parent;

    private HashMap<String, Variable> variablesInBlock; // in case of multiple variables with the same name,
    // needs to refer to the one in the most specific scope. probably a job of the method creating this
    // hashmap

    /**
     * The constructor of the block. It receives a hashmap of the variables in it's scope (and in outer
     * scopes)
     * @param variablesInBlock The hashmap of variables
     * @param parent the parent block of the current block
     */
    Block(HashMap<String, Variable> variablesInBlock, Block parent){
        this.variablesInBlock = variablesInBlock;
        this.parent = parent;
    }

    /**
     * A getter for the variables
     * @return the variables of the block
     */
    public HashMap<String, Variable> getVariablesInBlock() {
        return variablesInBlock;
    }
}
