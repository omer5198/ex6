package oop.ex6.main.parsing;

import oop.ex6.exceptions.condition.InvalidConditionException;
import oop.ex6.exceptions.method.InvalidMethodException;
import oop.ex6.exceptions.method.MethodException;
import oop.ex6.exceptions.parsing.InvalidLineException;
import oop.ex6.exceptions.variable.VariableException;
import oop.ex6.tools.Tuple;
import oop.ex6.validators.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	// This is a regex for validating a variable name
    public static final String VALID_VARIABLE_NAME_REGEX = "(?:_[A-Za-z_\\d]+|[A-Za-z]+[A-Za-z_\\d]*)";

    // This is a regex for the type of the variable or method
    public static final String CHECK_TYPES_REGEX = "(?:String|double|int|boolean|char)";

    // This is a regex matches variable decalring
    public static final String VARIABLE_DECLARING_REGEX = "\\s*(?:(final)\\s+)?(" + CHECK_TYPES_REGEX +
			")\\s+(?:\\s*" + VALID_VARIABLE_NAME_REGEX + "(?>(?:\\s*=\\s*\\S.*?)?\\s*[,;]))+(?<=;)\\s*";

    // This is a regex matching a variable assigning
    public static final String VARIABLE_ASSIGN_REGEX = "(?:_[A-Za-z_\\d]+|[A-Za-z]+" +
			"[A-Za-z_\\d]*)(?:\\s*=\\s*\\S.*?);$";

    // This is a pattern for variable assigning
    public static final Pattern VARIABLE_ASSIGN_PATTERN = Pattern.compile(VARIABLE_ASSIGN_REGEX);

    public static final Pattern VARIABLE_DECLARING_PATTERN = Pattern.compile(VARIABLE_DECLARING_REGEX);

    // This regex matches an if or while regex
    public static final String IF_OR_WHILE_REGEX = "\\s*(?:if|while)\\s*\\((?:\\s*[a-zA-Z]+[a-zA-Z0-9_]*" +
			"|_+\\s*[a-zA-Z0-9_]+|\\s*(?:-?\\s*\\d+|-?\\s*\\d+\\.\\d+))\\s*(?:(?:\\|\\||&&)\\s*" +
			"(?:[a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+|\\s*" +
			"(?:-?\\s*\\d+|-?\\s*\\d+\\.\\d+))\\s*)*\\)\\s*\\{\\s*";

    // the pattern of the previous regex
    public static final Pattern IF_OR_WHILE_PATTERN = Pattern.compile(IF_OR_WHILE_REGEX);

    // This regex matches a method declaring line
    public static final String METHOD_DECLARING_REGEX = "\\s*void\\s+([a-zA-Z]+[a-zA-Z\\d_]*)" +
			"\\s*\\(\\s*(?:(?:final\\s+)?\\s*(?:(?:String|double|int|boolean|char)\\s+" +
			"(?:_[A-Za-z_\\d]+|[A-Za-z]+[A-Za-z_\\d]*)\\s*,))*(?:\\s*(?:final\\s+)?\\s*" +
			"(?:(?:String|double|int|boolean|char)\\s+(" +
			"?:_[A-Za-z_\\d]+|[A-Za-z]+[A-Za-z_\\d]*)\\s*))?\\)\\s*\\{\\s*";


    // the pattern of the previous regex
    public static final Pattern METHOD_DECLARING_PATTERN = Pattern.compile(METHOD_DECLARING_REGEX);

    // This is a regex matching a method call
    public static final String METHOD_CALL_REGEX = "\\s*([a-zA-Z]+[a-zA-Z0-9_])*\\s*\\(\\s*(?:(?:\".*?\"|"+
            "\\s*\\d+\\s*|\\s*\\d+\\.\\d+\\s*|\\s*(?:true|false)|'.??'|(?:[a-zA-Z]+[a-zA-Z0-9_]*|" +
            "_+[a-zA-Z0-9_]+))\\s*(?:\\s*,\\s*(?:\".*?\"|\\s*\\d+\\s*|\\s*\\d+\\.\\d+\\s*|\\s*" +
            "(?:true|false)|'.??'|(?:[a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+)))*?|)\\s*\\);\\s*$";

    // this is a pattern for a method call line
    public static final Pattern METHOD_CALL_PATTERN = Pattern.compile(METHOD_CALL_REGEX);

    // This is a regex matching closing a block
    public static final String CLOSING_BLOCK_REGEX = "\\s*}\\s*";

    // This is a pattern for the previous regex
	public static final Pattern CLOSING_BLOCK_PATTERN = Pattern.compile(CLOSING_BLOCK_REGEX);

	// This is a regex for a return statement
    public static final String RETURN_REGEX = "\\s*return\\s*;\\s*";

    // This is a pattern for the previous regex
    public static final Pattern RETURN_PATTERN = Pattern.compile(RETURN_REGEX);

    // This is a msg for an invalid line
    public static final String INVALID_LINE_ERROR = "Invalid line provided";

    // this is a msg for a method that already exists
    public static final String METHOD_ALREADY_EXISTS_ERROR =
			"More than one method with the same name found.";

    // This is a msg for an invalid method structure
    public static final String INVALID_METHOD_STRUCTURE_ERROR = "Invalid method structure";

    // This is the suffix for the missing return statement
    public static final String MISSING_RETURN_ERROR_SUFFIX = " (Missing return statement at " +
			"the end of the method)";

    // This is a message for a variable that already exists
	public static final String ALREADY_EXIST_MSG = "Tried to create a variable which already exists";

	/**
	 * This method parses the global block
	 * @param block The global block
	 * @return A hashmap of names to methods
	 * @throws VariableException upon variable exception
	 * @throws InvalidLineException upon line exception
	 * @throws InvalidMethodException upon invalid method exception
	 */
    public static HashMap<String, Method> parseGlobalBlock(Block block) throws VariableException,
			InvalidLineException, InvalidMethodException {
		HashMap<String, Method> methodsMap = new HashMap<>();
        for(Tuple<String, Integer> line : block.getCodeLines()) {
			if(matchMethod(line, methodsMap)) {
				continue;
			}
			if(matchVariables(line, block)) {
				continue;
			}
			throw new InvalidLineException(INVALID_LINE_ERROR +
					" | Line " + String.valueOf(line.getSecond()));
		}
		return methodsMap;
    }

	/**
	 * This method parses a regular block
	 * @param block The block to parse
	 * @param methodHashMap The methods existing
	 * @throws VariableException upon variables exception
	 * @throws InvalidConditionException uppon invalid condition exception
	 * @throws MethodException upon method exception
	 * @throws InvalidLineException upon invalid line
	 */
    public static void parseBlock(Block block, HashMap<String, Method> methodHashMap)
			throws VariableException, InvalidConditionException, MethodException, InvalidLineException {
    	boolean isMethod = block.isMethod();
    	ArrayList<Tuple<String, Integer>> lines = block.getCodeLines();
		Tuple<String, Integer> firstLine = lines.get(0);
		if(isMethod) {
			try {
				Tuple<String, Integer> beforeLastLine = lines.get(lines.size() - 2);
				if(!matchReturn(beforeLastLine)) {
					throw new InvalidMethodException(INVALID_METHOD_STRUCTURE_ERROR +
							MISSING_RETURN_ERROR_SUFFIX);
				}
			}
			catch(IndexOutOfBoundsException e) {
				// This cannot really reach here because of the way we built our blocks
				throw new InvalidMethodException(INVALID_METHOD_STRUCTURE_ERROR);
			}
			getParameters(firstLine, block);
			lines.remove(0); // removes the first line
		}
    	for(Tuple<String, Integer> line : lines) {
    		if(matchVariables(line, block)) {
    			continue;
			}
			if(matchCondition(line, block)) {
    			continue;
			}
			if(matchMethodCall(line, block, methodHashMap)) {
    			continue;
			}
			if(matchReturn(line)) {
    			continue;
			}
			if(matchClosing(line)) {
    			continue;
			}
			throw new InvalidLineException(INVALID_LINE_ERROR +
					" | Line " + String.valueOf(line.getSecond()));
		}
		if(isMethod) {
			lines.add(0, firstLine);
		}
	}

	/**
	 * This method matches a return message
	 * @param line The line to match
	 * @return true iff there is a match
	 */
	private static boolean matchReturn(Tuple<String, Integer> line) {
    	Matcher returnMatcher = RETURN_PATTERN.matcher(line.getFirst());
    	return returnMatcher.matches();
	}

	/**
	 * This method matches a method call
	 * @param line the line to match
	 * @param block the block of the line
	 * @param methodHashMap The methods existing
	 * @return true iff there is a match
	 * @throws MethodException upon method exception
	 * @throws VariableException upon variable exception
	 */
	private static boolean matchMethodCall(Tuple<String, Integer> line, Block block,
										   HashMap<String, Method> methodHashMap)
			throws MethodException, VariableException {
    	Matcher methodCallMatcher = METHOD_CALL_PATTERN.matcher(line.getFirst());
    	if(methodCallMatcher.matches()) {
    		String methodName = methodCallMatcher.group(1);
    		MethodCallValidator.valdiateMethodCall(line, methodName, block, methodHashMap);
    		return true;
		}
		return false;
	}

	/**
	 * This method matches a condition
	 * @param line the line to match
	 * @param block the block of the line
	 * @return true iff there is a match
	 * @throws InvalidConditionException upon invalid condition
	 * @throws VariableException upon variable condition
	 */
	private static boolean matchCondition(Tuple<String, Integer> line, Block block)
			throws InvalidConditionException, VariableException {
		Matcher conditionMatcher = IF_OR_WHILE_PATTERN.matcher(line.getFirst());
		if(conditionMatcher.matches()) {
			ConditionValidator.validateCondition(line, block);
			return true;
		}
		return false;
	}

	/**
	 * This method gets the parameters from a method declaration
	 * @param line the line of the declaration
	 * @param block the block of the line
	 * @throws VariableException upon variable exception
	 */
	private static void getParameters(Tuple<String, Integer> line, Block block) throws VariableException {
		Matcher methodMatcher = METHOD_DECLARING_PATTERN.matcher(line.getFirst());
		if(methodMatcher.matches()) {
			ArrayList<Variable> parameters = MethodParser.parseMethodVariables(line);
			for(Variable param : parameters) {
				if(block.getLocalVariable(param.getName()) != null) {
					// in case the variable already exists in this scope
					throw new VariableException(ALREADY_EXIST_MSG + " | Line " + line.getSecond());
				}
				block.addVariable(param);
			}
		}
	}


	/**
	 * This method matches a method declaring
	 * @param line the line to match
	 * @param methodsMap the method existing
	 * @return true iff there is a match
	 * @throws InvalidMethodException upon invalid method
	 */
	private static boolean matchMethod(Tuple<String, Integer> line,
									   HashMap<String, Method> methodsMap) throws InvalidMethodException {
		Matcher methodMatcher = METHOD_DECLARING_PATTERN.matcher(line.getFirst());
		if(methodMatcher.matches()) {
			String methodName = methodMatcher.group(1);
			Method method = MethodParser.getMethod(line, methodName);
			if(methodsMap.get(methodName) != null) {
				throw new InvalidMethodException(METHOD_ALREADY_EXISTS_ERROR + " | Line " +
						line.getSecond());
			}
			methodsMap.put(methodName, method);
			return true;
		}
		return false;
	}

	/**
	 * This method matches a variable declaring
	 * @param line the line to match
	 * @param block the block of the line
	 * @return true iff there is a match
	 * @throws VariableException in case trying to create a taken variable name
	 */
	private static boolean matchVariables(Tuple<String, Integer> line, Block block)
			throws VariableException {
		Matcher variableDeclareMatcher = VARIABLE_DECLARING_PATTERN.matcher(line.getFirst());
		if(variableDeclareMatcher.matches()) {
			boolean isFinal = variableDeclareMatcher.group(1) != null;
			String type = variableDeclareMatcher.group(2);
			ArrayList<Variable> variables = VariableValidator.getVariables(line, block, type, isFinal);
			for(Variable var : variables) {
				if(block.getLocalVariable(var.getName()) != null) {
					// in case the variable already exists in this scope
					throw new VariableException(ALREADY_EXIST_MSG + " | Line " + line.getSecond());
				}
				block.addVariable(var);
			}
			return true;
		}
		Matcher variableAssignMatcher = VARIABLE_ASSIGN_PATTERN.matcher(line.getFirst());
		if(variableAssignMatcher.matches()) {
			ArrayList<Variable> variables = VariableValidator.getVariables(line, block, null, false);
			if(variables != null) {
				for(Variable var : variables) {
					if(block.getLocalVariable(var.getName()) != null) {
						// in case the variable already exists in this scope
						throw new VariableException(ALREADY_EXIST_MSG + " | Line " + line.getSecond());
					}
					block.addVariable(var);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * This method matches the closing of a line
	 * @param line the line to match
	 * @return true iff there is a match
	 */
	private static boolean matchClosing(Tuple<String, Integer> line) {
    	Matcher closingMatcher = CLOSING_BLOCK_PATTERN.matcher(line.getFirst());
    	return closingMatcher.matches();
	}
}
