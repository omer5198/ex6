package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class scans the file of code we recieve. It handles basic exceptions (such as methods defined in
 * illegal places) and saves all variables and methods declared and\or defined in the file. It saves
 * the variables in blocks, which enables us to take scopes under consideration later on.
 */


public class InitialScanner {

    private ArrayList<String> linesToScan;

    private int unclosedBlocks;

    private int lineNumber;

    private HashMap<String, Variable> globalVariables; // we need this so that after foing initial scanning
    // we would have access to the global variables since the global scope is not a block (and method returns
    // list of blocks

    /**
     * This is the constructor of the class.
     * @param linesToScan The list of lines from the file. We receive these from the file reader.
     *
     */
    public InitialScanner(ArrayList<String> linesToScan) {
        this.linesToScan = linesToScan;
        this.unclosedBlocks = 0;
        lineNumber = 0;
    }


    /**
     * The main method of the class. It reads the lines given in the constructor and returns a list of all
     * blocks of the file
     * @return A list of the blocks of the file. the global scope block is the last block in the list.
     * @throws InvalidBracketsException When the bracket opening and closing is invalid
     * @throws MethodInInnerScopeException When a method is defined not int the outer scope
     * @throws MethodCallInOuterScopeException When a method is called not inside a method declaration.
     */
    public Tuple<Block, ArrayList<Block>> InitialParse() throws InvalidBracketsException, MethodInInnerScopeException,
            MethodCallInOuterScopeException{
        HashMap<String, Variable> globalVariables = new HashMap<String, Variable>();
        String currLine;
        Block currBlock = new Block(new HashMap<>(), null, new ArrayList<>(), false);
        Block globalBlock = currBlock;
        Block temp = null;
        boolean addAtEnd = true;
        ArrayList<Block> blocksList = new ArrayList<>();
        while (lineNumber < linesToScan.size()) {
            currLine = linesToScan.get(lineNumber); // check here to allow while check condition
            if (!(isLineEmpty(currLine) || isLineComment(currLine))) {
                if (isLineClosingBlock(currLine)){
                    unclosedBlocks --;
                    if(unclosedBlocks < 0){
                        throw new InvalidBracketsException("invalid brackets closing");
                    }
                    currBlock.addLine(new Tuple<>(currLine, lineNumber));
                    blocksList.add(currBlock);
                    currBlock = currBlock.getParent(); // curBlock is null iff it is in the outer scope, there
                    // can be no closing of a block in the outer scope
                    addAtEnd = false;
                }
                else if (isLineOpeningBlock(currLine)) {
                    unclosedBlocks++;
                    globalBlock.addLine(new Tuple<>(currLine, lineNumber));
                    addAtEnd = true;
                    // block opening
                    if(isLineIfOrWhile(currLine)){
                        temp = currBlock;
                        currBlock = new Block(new HashMap<>(), temp, new ArrayList<>(),
                                false);
                    } else if (isLineMethodDeclaration(currLine)){
                        if(currBlock.getParent() != null) {
                            throw new MethodInInnerScopeException("method declared not in outer scope");
                        }
                        temp = currBlock;
                        currBlock = new Block(new HashMap<>(), temp, new ArrayList<>(),
                                true);
                    }

                } else if (isLineVariableDeclaration(currLine)) {
                        currBlock.addDeclaredVariables(currLine);
                    }
                } else if (isLineMethodCall(currLine)){
                    if(!isBlockInMethod(currBlock)){
                        throw new MethodCallInOuterScopeException("method called in outer scope");
                    }
                }
                if (addAtEnd) {
                    currBlock.addLine(new Tuple<>(currLine, lineNumber));
                } // notice that if a block was opened the
            // line will be saved in the new opened block. otherwise, in the current.
            lineNumber ++;
        }
        if(unclosedBlocks != 0){
            throw new InvalidBracketsException("invalid brackets closing" + lineNumber);
        }
        this.globalVariables = globalVariables;

        return new Tuple(globalBlock, blocksList);
    }



    //TODO put in actual regexes
    private boolean isLineVariableDeclaration(String line){
        return matchRegex(VARIABLE_DECLARATION_REGEX, line);
    }

    private boolean isLineMethodCall(String currLine) {
        return matchRegex(METHOD_CALL_REGEX, currLine);
    }

    private boolean isLineOpeningBlock(String line){
        return matchRegex(OPENING_BLOCK_REGEX, line);
    }

    private boolean isLineClosingBlock(String line){
        return matchRegex(CLOSING_BLOCK_REGEX, line);
    }

    private boolean isLineEmpty(String line){
        return matchRegex(EMPTY_LINE_REGEX, line);
    }

    private boolean isLineMethodDeclaration(String line){
        return matchRegex(METHOD_DECLARATION_REGEX, line);
    }

    private boolean isLineIfOrWhile(String line){
        return matchRegex(IF_OR_WHILE_REGEX, line);
    }

    private boolean isLineComment(String line) {
        return matchRegex(COMMENT_REGEX, line);
    }

    //TODO implement
    private HashMap<String, Variable> getMethodParameters(String line){
        return new HashMap<>();
    }

    /**
     * This methods receives a line as a string and returns if the line matches a certain regex.
     * @param regex The given regex we wish to match the line to
     * @param line The line we are matching
     * @return true iff the line matches the given regex.
     */
    private boolean matchRegex(String regex, String line){
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(line);
        return m.matches();
    }

    //TODO implement
    public void addDeclaredVariables(String line){

    }

    /**
     * This method checks if a certain block is in a scope of a method declaration, this helps us determine
     * if a method call is legal or not.
     * @param startingBlock The block we check
     * @return true iff the block is somewhere in a method defining block
     */
    private boolean isBlockInMethod(Block startingBlock){
        Block currBlock = startingBlock;
        while(currBlock.getParent() != null){
            if (currBlock.isMethod()){
                return true;
            }
        currBlock = currBlock.getParent();
        }
        return false;

    }

    public HashMap<String, Variable> getGlobalVariables() {
        return globalVariables;
    }
}
