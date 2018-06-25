package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodCallValidator {

	private static final String INVALID_NUMBER_OF_PARAMS_ERROR = "Invalid number of arguments provided";

	private static final String INVALID_PARAMETER_TYPE = "Invalid parameter type provided";

	private static final String INVALID_METHOD_NAME = "Invalid method name provided";

	private static class MethodCallParser {

		final static private String PARAMETERS_SPLIT_REGEX =
				"[\\(,]?\\s*+(.+?)\\s*[,\\)]";

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

	public void valdiateMethodCall(Tuple<String, Integer> line, String methodName, Block block,
								   HashMap<String, Method> methodHashMap)
			throws ParameterException, InvalidMethodException {
		String text = line.getFirst();
		int lineNumber = line.getSecond();
		String msgSuffix = " | Line " + String.valueOf(lineNumber);
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
}
