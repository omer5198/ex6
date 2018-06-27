package oop.ex6.main.parsing;

import oop.ex6.exceptions.condition.ConditionException;
import oop.ex6.exceptions.condition.ConditionInOuterScopeError;
import oop.ex6.exceptions.method.MethodException;
import oop.ex6.exceptions.method.MethodInInnerScopeException;
import oop.ex6.exceptions.parsing.InvalidBracketsException;
import oop.ex6.tools.Tuple;
import oop.ex6.validators.Variable;

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

    // The prefix of a comment in javas
    public static final String COMMENT_PREFIX = "//";

    // The message of the invalid brackets error in the middle of the code
    public static final String INVALID_BRACKETS_ERROR = "Invalid brackets closing";

    // The message of the invalid brackets error in the end
    public static final String INVALID_BRACKETS_STRUCT_ERROR = "Invalid brackets structure";

    // The message of the method declared in inner scope exception
    public static final String METHOD_DECLARED_IN_INNER_SCOPE_ERROR =
			"Method declared not in outer scope";


    // regex of an empty line
	public static final String EMPTY_LINE_REGEX = "\\s*";

	// pattern for empty line regex
	public static final Pattern EMPTY_LINE_PATTERN = Pattern.compile(EMPTY_LINE_REGEX);

	// error message for condition defined in outer scope
	public static final String CONDITION_IN_OUTER_SCOPE_ERROR = "If / While found in global scope";

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
     * The main method of the class. It reads the lines given in the constructor and returns a tuple
     * containing the global block in its final state (containing all methods and all global variables),
     * plus a list of all the inner blocks (scopes) of the code file. It handles basic exceptions such
     * as a method not defined in the outer scope or a condition defined in the outer scope.
     * @return A list of the blocks of the file. the global scope block is the last block in the list.
     * @throws InvalidBracketsException When the bracket opening and closing is invalid
     * @throws MethodInInnerScopeException When a method is defined not int the outer scope
     */
    public Tuple<Block, ArrayList<Block>> initialParse() throws InvalidBracketsException,
			MethodException, ConditionException {
        HashMap<String, Variable> globalVariables = new HashMap<String, Variable>();
        String currLine;
        Block currBlock = new Block(null, false);
        Block globalBlock = currBlock;
        Block temp = null;
        boolean addAtEnd = true; // sometimes we want to add the line immediately and sometimes to the next
        // block
        ArrayList<Block> blocksList = new ArrayList<>();
        while (lineNumber < linesToScan.size()) {
            currLine = linesToScan.get(lineNumber); // check here to allow while check condition
			String msgSuffix = " | Line " + String.valueOf(lineNumber);
            if (!(isLineEmpty(currLine) || isLineComment(currLine))) {
				if (isLineClosingBlock(currLine)) {
					unclosedBlocks--;
					if (unclosedBlocks < 0) {
						throw new InvalidBracketsException(INVALID_BRACKETS_ERROR + msgSuffix);
					}
					currBlock.addLine(new Tuple<>(currLine, lineNumber));
					blocksList.add(0, currBlock);
					/* curBlock is null iff it is in the outer scope,
					there can be no closing of a block in the outer scope */
					currBlock = currBlock.getParent();
					addAtEnd = false;
				} else {
					addAtEnd = true;
					// block opening
					if (isLineIfOrWhile(currLine)) {
						if(!isBlockInMethod(currBlock)) {
							throw new ConditionInOuterScopeError(
									CONDITION_IN_OUTER_SCOPE_ERROR + msgSuffix);
						}
						temp = currBlock;
						unclosedBlocks++;
						currBlock = new Block(temp, false);
					} else if (isLineMethodDeclaration(currLine)) {
						globalBlock.addLine(new Tuple<>(currLine, lineNumber));
						if (currBlock.getParent() != null) {
							throw new MethodInInnerScopeException(
									METHOD_DECLARED_IN_INNER_SCOPE_ERROR + msgSuffix);
						}
						temp = currBlock;
						unclosedBlocks++;
						currBlock = new Block(temp, true);
					}
				}
				if (addAtEnd) {
					currBlock.addLine(new Tuple<>(currLine, lineNumber));
				}
			}
            // line will be saved in the new opened block. otherwise, in the current.
            lineNumber ++;
        }
        if(unclosedBlocks != 0){
            throw new InvalidBracketsException(INVALID_BRACKETS_STRUCT_ERROR);
        }

        return new Tuple(globalBlock, blocksList);
    }

    private boolean isLineVariableDeclaration(String line){
        return matchPattern(Parser.VARIABLE_DECLARING_PATTERN, line);
    }

    private boolean isLineClosingBlock(String line){
        return matchPattern(Parser.CLOSING_BLOCK_PATTERN, line);
    }

    private boolean isLineEmpty(String line){
        return matchPattern(EMPTY_LINE_PATTERN, line);
    }

    private boolean isLineMethodDeclaration(String line){
        return matchPattern(Parser.METHOD_DECLARING_PATTERN, line);
    }

    private boolean isLineIfOrWhile(String line){
        return matchPattern(Parser.IF_OR_WHILE_PATTERN, line);
    }

    private boolean isLineComment(String line) {
        return line.startsWith(COMMENT_PREFIX);
    }

    /**
     * This methods receives a line as a string and returns if the line matches a certain regex.
     * @param line The line we are matching
	 * @param pattern the pattern to match the line with
     * @return true iff the line matches the given pattern
     */
    private boolean matchPattern(Pattern pattern, String line){
        Matcher m = pattern.matcher(line);
        return m.matches();
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
}
