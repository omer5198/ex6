package oop.ex6.main;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final String VALID_VARIABLE_NAME_REGEX = "(?:_[A-Za-z_\\d]+|[A-Za-z]+[A-Za-z_\\d]*)";

    private static final String VALID_METHOD_NAME_REGEX = "[a-zA-Z]+[a-zA-Z\\d_]*";

    public static String CHECK_TYPES_REGEX = "(?:String|double|int|boolean|char)";

    private static final String METHOD_PARAMETER_REGEX = "\\s*" + CHECK_TYPES_REGEX + "\\s+" +
			VALID_VARIABLE_NAME_REGEX;

    private static final String VARIABLE_DECLARING_REGEX = "\\s*(?:(final)\\s+)?(" + CHECK_TYPES_REGEX +
			")\\s+(?:\\s*(" + VALID_VARIABLE_NAME_REGEX + ")(?>(?:\\s*\\=\\s*[^\\s]+?)?\\s*[,;]))+(?<=;)$";

    static final Pattern VARIABLE_DECLARING_PATTERN = Pattern.compile(VARIABLE_DECLARING_REGEX);

    private static final String IF_OR_WHILE_REGEX = "\\s*(?:if|while)\\s*\\((?:\\s*[a-zA-Z]+[a-zA-Z0-9_]*|_+ " +
            "[a-zA-Z0-9_]+|\\s*(?:\\d+|\\d+\\.\\d+))\\s*(?:(?:\\|\\||&&)\\s*(?:[a-zA-Z]+[a-zA-Z0-9_]*|" +
            "_+[a-zA-Z0-9_]+|\\s*(?:\\d+|\\d+\\.\\d+))\\s*)*\\)\\s*\\{\\s*";

    static final Pattern IF_OR_WHILE_PATTERN = Pattern.compile(IF_OR_WHILE_REGEX);

    private static final String METHOD_DECLARING_REGEX = "\\s*void\\s+([a-zA-Z]+[a-zA-Z\\d_]*)\\s*\\((?:\\s*" +
			"(?:final\\s+)?\\s*(?:(?:String|double|int|boolean|char)\\s+(?:_[A-Za-z_\\d]+|[A-Za-z]+" +
			"[A-Za-z_\\d]*)\\s*,))*(?:\\s*(?:final\\s+)?\\s*(?:(?:String|double|int|boolean|char)\\s+(" +
			"?:_[A-Za-z_\\d]+|[A-Za-z]+[A-Za-z_\\d]*)\\s*))?\\)\\s*\\{\\s*";

    static final Pattern METHOD_DECLARING_PATTERN = Pattern.compile(METHOD_DECLARING_REGEX);

    private static final String METHOD_CALL_REGEX = "\\s*([a-zA-Z]+[a-zA-Z0-9_])*\\s*\\(\\s*(?:(?:\".*?\"|" +
            "\\s*\\d+\\s*|\\s*\\d+\\.\\d+\\s*|\\s*(?:true|false)|'.??'|(?:[a-zA-Z]+[a-zA-Z0-9_]*|" +
            "_+[a-zA-Z0-9_]+))\\s*(?:\\s*,\\s*(?:\".*?\"|\\s*\\d+\\s*|\\s*\\d+\\.\\d+\\s*|\\s*" +
            "(?:true|false)|'.??'|(?:[a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+)))*?|)\\s*\\);\\s*$";

    private static final Pattern METHOD_CALL_PATTERN = Pattern.compile(METHOD_CALL_REGEX);

    private static final String RETURN_REGEX = "\\s*return\\s*;\\s*";

    private static final Pattern RETURN_PATTERN = Pattern.compile(RETURN_REGEX);

    private static final String INVALID_LINE_ERROR = "Invalid line provided";

    private static final String INVALID_METHOD_STRUCTURE_ERROR = "Invalid method structure";

    private static final String MISSING_RETURN_ERROR_SUFFIX = " (Missing return statement at " +
			"the end of the method)";

    public static HashMap<String, Method> parseGlobalBlock(Block block) throws VariableException, InvalidLineException {
		HashMap<String, Method> methodsMap = new HashMap<>();
		String msgSuffix;
        for(Tuple<String, Integer> line : block.getCodeLines()) {
			if(matchMethod(line, block, methodsMap)) {
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

    public static void parseBlock(Block block, HashMap<String, Method> methodHashMap)
			throws VariableException, InvalidConditionException, InvalidMethodException,
			ParameterException, MethodCallInOuterScopeException {
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
		}
		if(isMethod) {
			lines.add(0, firstLine);
		}
	}

	private static boolean matchReturn(Tuple<String, Integer> line) {
    	Matcher returnMatcher = RETURN_PATTERN.matcher(line.getFirst());
    	return returnMatcher.matches();
	}

	private static boolean matchMethodCall(Tuple<String, Integer> line, Block block,
										   HashMap<String, Method> methodHashMap)
			throws InvalidMethodException, ParameterException, MethodCallInOuterScopeException {
    	Matcher methodCallMatcher = METHOD_CALL_PATTERN.matcher(line.getFirst());
    	if(methodCallMatcher.matches()) {
    		String methodName = methodCallMatcher.group(1);
    		MethodCallValidator.valdiateMethodCall(line, methodName, block, methodHashMap);
    		return true;
		}
		return false;
	}

	private static boolean matchCondition(Tuple<String, Integer> line, Block block)
			throws InvalidConditionException {
		Matcher conditionMatcher = IF_OR_WHILE_PATTERN.matcher(line.getFirst());
		if(conditionMatcher.matches()) {
			ConditionValidator.validateCondition(line, block);
			return true;
		}
		return false;
	}

	private static void getParameters(Tuple<String, Integer> line, Block block) {
		Matcher methodMatcher = METHOD_DECLARING_PATTERN.matcher(line.getFirst());
		if(methodMatcher.matches()) {
			ArrayList<Variable> parameters = MethodParser.parseMethodVariables(line);
			for(Variable param : parameters) {
				block.addVariable(param);
			}
		}
	}


	private static boolean matchMethod(Tuple<String, Integer> line, Block block,
									   HashMap<String, Method> methodsMap) {
		Matcher methodMatcher = METHOD_DECLARING_PATTERN.matcher(line.getFirst());
		if(methodMatcher.matches()) {
			String methodName = methodMatcher.group(1);
			Method method = MethodParser.getMethod(line, methodName);
			methodsMap.put(methodName, method);
			return true;
		}
		return false;
	}

	private static boolean matchVariables(Tuple<String, Integer> line, Block block)
			throws VariableException {
		Matcher variableMatcher = VARIABLE_DECLARING_PATTERN.matcher(line.getFirst());
		if(variableMatcher.matches()) {
			boolean isFinal = variableMatcher.group(1) != null;
			String type = variableMatcher.group(2);
			ArrayList<Variable> variables = VariableValidator.getVariables(line, block, type, isFinal);
			for(Variable var : variables) {
				block.addVariable(var);
			}
			return true;
		}
		return false;
	}
}
