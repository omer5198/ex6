package oop.ex6.validators;

import oop.ex6.main.parsing.Parser;
import oop.ex6.tools.Tuple;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodParser {

	final static private String PARAMETER_SPLIT_REGEX = "\\b(?:(final)\\s+)?(" +
			Parser.CHECK_TYPES_REGEX + ")\\s+((?:_[A-Za-z_\\d]+|[A-Za-z]+[A-Za-z_\\d]*))\\s*[,\\)]";

	final static private Pattern PARAMETER_SPLIT_PATTERN = Pattern.compile(PARAMETER_SPLIT_REGEX);

	public static ArrayList<Variable> parseMethodVariables(Tuple<String, Integer> line) {
		ArrayList<Variable> variables = new ArrayList<>();
		String text = line.getFirst();
		int lineNumber = line.getSecond();
		Matcher matcher = PARAMETER_SPLIT_PATTERN.matcher(text);
		while(matcher.find()) {
			boolean isFinal = matcher.group(1) != null;
			String type = matcher.group(2);
			String name = matcher.group(3);
			variables.add(new Variable(type, name, lineNumber, false, isFinal));
		}
		return variables;
	}

	public static Method getMethod(Tuple<String, Integer> line, String methodName) {
		ArrayList<String> parameters = new ArrayList<>();
		String text = line.getFirst();
		Matcher matcher = PARAMETER_SPLIT_PATTERN.matcher(text);
		while(matcher.find()) {
			String type = matcher.group(2);
			parameters.add(type);
		}
		return new Method(methodName, parameters);
	}
}
