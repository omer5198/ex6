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

	/**
	 * This parses a method declaration line and returns a list of all variables that the method receives
	 * as input.
	 * @param line the line that we are parsing (a tuple of text and line number)
	 * @return the arraylist of variables, holding only the type and the name of the variable
	 */
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

	/**
	 * This method returns a method object, out of the name of the method and the line in which it is
	 * declared
	 * @param line the line in which the method is declared
	 * @param methodName the name of the method
	 * @return A new appropriate method object with the relevant name and input parameters
	 */
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
