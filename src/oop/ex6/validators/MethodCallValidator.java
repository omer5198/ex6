package oop.ex6.validators;

import oop.ex6.exceptions.method.*;
import oop.ex6.exceptions.variable.UninitializedException;
import oop.ex6.exceptions.variable.VariableException;
import oop.ex6.main.parsing.Block;
import oop.ex6.tools.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodCallValidator {

	public static final String INVALID_NUMBER_OF_PARAMS_ERROR = "Invalid number of arguments provided";

	public static final String INVALID_PARAMETER_TYPE = "Invalid parameter type provided";

	public static final String INVALID_METHOD_NAME = "Invalid method name provided";

	public static final String METHOD_IN_OUTER_SCOPE_ERROR = "Method called not inside a method";

	private static class MethodCallParser {

		final static private String PARAMETERS_SPLIT_REGEX =
				"[\\(,]?\\s*+([^\\(]+?)\\s*[,\\)]";

		final static private Pattern PARAMETERS_SPLIT_PATTERN = Pattern.compile(PARAMETERS_SPLIT_REGEX);

		public static ArrayList<String> parseParameters(String line) {
			ArrayList<String> params = new ArrayList<>();
			Matcher matcher = PARAMETERS_SPLIT_PATTERN.matcher(line);
			while(matcher.find()) {
				params.add(matcher.group(1));
			}
			return params;
		}
	}

	public static void valdiateMethodCall(Tuple<String, Integer> line, String methodName, Block block,
										  HashMap<String, Method> methodHashMap)
			throws MethodException, VariableException{
		String text = line.getFirst();
		int lineNumber = line.getSecond();
		String msgSuffix = " | Line " + String.valueOf(lineNumber);
		if(!isBlockInMethod(block)) {
			throw new MethodCallInOuterScopeException(METHOD_IN_OUTER_SCOPE_ERROR + msgSuffix);
		}
		Method method = getMethod(methodName, methodHashMap);
		if(method == null) {
			throw new InvalidMethodException(INVALID_METHOD_NAME + msgSuffix);
		}
		ArrayList<String> paramsStruct = method.getParameterTypes();
		ArrayList<String> params = MethodCallParser.parseParameters(text);
		if(paramsStruct.size() != params.size()) {
			throw new InvalidNumberOfParamsException(INVALID_NUMBER_OF_PARAMS_ERROR + msgSuffix);
		}
		for(int i = 0; i < params.size(); i++) {
			String expectedType = paramsStruct.get(i);
			String param = params.get(i);
			Variable var = block.getVariable(param);
			String type;
			if(var != null) {
				if(!var.isInitialized()) {
					throw new UninitializedException(VariableValidator.UNINITIALIZED_VAR_MSG + msgSuffix);
				}
				if(lineNumber < var.getLine() && !var.isGlobal()) {
					// in case of trying to reach a variable that wasn't defined yet
					throw new VariableException(VariableValidator.INVALID_VAR_MSG + msgSuffix);
				}
				type = var.getType();
			}
			else {
				type = VariableValidator.getType(param);
			}
			try {
				VariableValidator.compareTypes(expectedType, type, msgSuffix);
			}
			catch(VariableException e) {
				throw new InvalidParameterException(INVALID_PARAMETER_TYPE + msgSuffix);
			}
		}
	}

	private static Method getMethod(String methodName, HashMap<String, Method> methodHashMap) {
		return methodHashMap.get(methodName);
	}

	/**
	 * This method checks if a certain block is in a scope of a method declaration, this helps us determine
	 * if a method call is legal or not.
	 * @param startingBlock The block we check
	 * @return true iff the block is somewhere in a method defining block
	 */
	static boolean isBlockInMethod(Block startingBlock){
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
