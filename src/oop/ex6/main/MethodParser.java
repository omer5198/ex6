package oop.ex6.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodParser {

	final static private String PARAMETER_SPLIT_REGEX = "\\b(?:(final)\\s+)?(" +
			Parser.CHECK_TYPES_REGEX + ")\\s+((?:_[A-Za-z_\\d]+|[A-Za-z]+[A-Za-z_\\d]*))\\s*[,\\)]";

	final static private Pattern PARAMETER_SPLIT_PATTERN = Pattern.compile(PARAMETER_SPLIT_REGEX);

	public static Tuple<ArrayList<Variable>, Method> parseMethod(Tuple<String, Integer> line,
																 String methodName) {
		ArrayList<Variable> variables = new ArrayList<>();
		ArrayList<String> parameters = new ArrayList<>();
		String text = line.getFirst();
		int lineNumber = line.getSecond();
		Matcher matcher = PARAMETER_SPLIT_PATTERN.matcher(text);
		while(matcher.find()) {
			boolean isFinal = matcher.group(1) != null;
			String type = matcher.group(2);
			String name = matcher.group(3);
			variables.add(new Variable(type, name, lineNumber, false, isFinal));
			parameters.add(type);
		}
		Method method = new Method(methodName, parameters);
		return new Tuple(variables, method);
	}
}
