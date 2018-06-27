package oop.ex6.validators;

/**
 * This class validates lines involving variables, such as declaring and assigning variables.
 */

import oop.ex6.exceptions.variable.*;
import oop.ex6.main.parsing.Block;
import oop.ex6.tools.Tuple;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableValidator {

	final static private String INT_ID = "int";

	final static private String STRING_ID = "String";

	final static private String CHAR_ID = "char";

	final static private String DOUBLE_ID = "double";

	final static private String BOOLEAN_ID = "boolean";

	final static private String INT_REGEX = "-?\\s*\\d+";

	final static private String STRING_REGEX = "\".*\"";

	final static private String CHAR_REGEX = "\'.?\'";

	final static private String DOUBLE_REGEX = "-?\\s*\\d+(?:\\.\\d+)?";

	final static private String BOOLEAN_REGEX = "\\s*(?:true|false|" + INT_REGEX + "|" +
			DOUBLE_REGEX + ")";

	final static String INVALID_TYPE_MSG = "Invalid type provided";

	final static String INVALID_VALUE_MSG = "Invalid value provided";

	final static String INVALID_FINAL_MSG = "Tried to modify final variable";

	final static String INVALID_VAR_MSG = "The variable (in the value) provided doesn't exist";

	final static String UNINITIALIZED_VAR_MSG = "Tried to use an uninitialized variable";

	final static String UNMATCHING_TYPE_MSG = "Variables types don't match";

	final static String ALREADY_EXIST_MSG = "Variable already exists in this scope";

	final static String UNINITIALIZED_FINAL_ERROR = "Uninitialized final exception provided";

	final static String MULTIPLE_ASSIGNMENT_ERROR = "Tried to assign values to multiple variables at once";

	final static private Pattern INT_PATTERN = Pattern.compile(INT_REGEX);

	final static private Pattern STRING_PATTERN = Pattern.compile(STRING_REGEX);

	final static private Pattern DOUBLE_PATTERN = Pattern.compile(DOUBLE_REGEX);

	final static private Pattern CHAR_PATTERN = Pattern.compile(CHAR_REGEX);

	final static private Pattern BOOLEAN_PATTERN = Pattern.compile(BOOLEAN_REGEX);

	private static class VariableParser {

		final static private String VAR_SPLIT_REGEX =
				"\\b((?:_[A-Za-z_\\d]+|[A-Za-z]+[A-Za-z_\\d]*))(?:\\s*=\\s*(.+?))?\\s*[,;]";

		final static private Pattern VAR_SPLIT_PATTERN = Pattern.compile(VAR_SPLIT_REGEX);

		/**
		 * This method receives a line with method declaration and seperates them to return an arraylist
		 * of tuples (with the name and value of the variable).
		 * @param line The line we wish to parse.
		 * @return A list of tuples of the variables names and values.
		 */
		public static ArrayList<Tuple<String, String>> parseVariable(String line) {
			ArrayList<Tuple<String, String>> variables = new ArrayList<>();
			Matcher matcher = VAR_SPLIT_PATTERN.matcher(line);
			while(matcher.find()) {
				String name = matcher.group(1);
				String value = matcher.group(2);
				variables.add(new Tuple(name, value));
			}
			return variables;
		}
	}

	/**
	 * This method receives the list of variable names and values, returns a list of variables if they are
	 * valid and throws exceptions when encountering them.
	 * @param line The line that we check for validity of the line
	 * @param block The block in which the line exists
	 * @param type The type of the variable declared (null if it is an assignment)
	 * @param isFinal whether the variables declaring is final or not
	 * @return an arraylist of all variable object defined in the line (which is either a declaration or
	 * assignment)
	 * @throws VariableException upon encountering any exception (invalidity) of variables in the line
	 */
	public static ArrayList<Variable> getVariables(Tuple<String, Integer> line, Block block, String type,
												   boolean isFinal)
			throws VariableException {
		ArrayList<Variable> variables = new ArrayList<>();
		String text = line.getFirst();
		int lineNumber = line.getSecond();
		String msgSuffix = " | Line " + String.valueOf(lineNumber);
		ArrayList<Tuple<String, String>> variablesTuples = VariableParser.parseVariable(text);
		for(Tuple<String, String> tuple : variablesTuples) {
			String name = tuple.getFirst();
			String value = tuple.getSecond();
			Pattern pattern;
			if (type != null) {
				// in case of creating variables
				if(block.getLocalVariable(name) != null) {
					// in case the variable already exists in this scope
					throw new VariableException(ALREADY_EXIST_MSG + msgSuffix);
				}
				try {
					pattern = getPattern(type);
				}
				catch(InvalidTypeException e) {
					// if the type isn't valid
					throw new InvalidTypeException(INVALID_TYPE_MSG + msgSuffix);
				}
				if(value != null) {
					Variable secondVariable = block.getVariable(value);
					if(secondVariable != null) {
						checkVariableUsability(secondVariable, lineNumber, msgSuffix);
						String type2 = secondVariable.getType();
						compareTypes(type, type2, msgSuffix);
					}
					else {
						Matcher matcher = pattern.matcher(value);
						if (!matcher.matches()) {
							// if the value doesn't match the type
							throw new InvalidValueException(INVALID_VALUE_MSG + msgSuffix);
						}
					}
					Variable newVariable = new Variable(type, name, lineNumber, block.getParent() == null,
							isFinal);
					variables.add(newVariable);
				}
				else {
					if(isFinal) {
						throw new FinalException(UNINITIALIZED_FINAL_ERROR + msgSuffix);
					}
					// in case value wasn't given (variable wasn't initialized)
					Variable newVariable = new Variable(type, name, Variable.UNINITIALIZED,
							block.getParent() == null, isFinal);
					variables.add(newVariable);
				}
			} else {
				if(variablesTuples.size() != 1) {
					throw new VariableException(MULTIPLE_ASSIGNMENT_ERROR + msgSuffix);
				}
				Variable variable = block.getVariable(name);
				if(variable.isFinal()) {
					// if trying to change a final variable
					throw new FinalException(INVALID_FINAL_MSG + msgSuffix);
				}
				pattern = getPattern(variable.getType());
				Variable secondVariable = block.getVariable(value);
				if(secondVariable != null) {
					checkVariableUsability(secondVariable, lineNumber, msgSuffix);
					String type1 = variable.getType();
					String type2 = secondVariable.getType();
					compareTypes(type1, type2, msgSuffix);
				}
				else {
					Matcher matcher = pattern.matcher(value);
					if(!matcher.matches()) {
						throw new InvalidValueException(INVALID_VALUE_MSG + msgSuffix);
					}
				}
				if(variable.isGlobal() && block.getParent() != null) {
					variables.add(new Variable(variable.getType(), name, lineNumber, false, false));
				}
				else {
					variable.setInitializationLine(lineNumber);
				}
			}
		}
		return variables;
	}

	private static Pattern getPattern(String type) throws InvalidTypeException{
		switch(type) {
			case INT_ID:
				return INT_PATTERN;
			case STRING_ID:
				return STRING_PATTERN;
			case CHAR_ID:
				return CHAR_PATTERN;
			case DOUBLE_ID:
				return DOUBLE_PATTERN;
			case BOOLEAN_ID:
				return BOOLEAN_PATTERN;
			default:
				throw new InvalidTypeException(INVALID_TYPE_MSG);
		}
	}

	static void compareTypes(String type1, String type2, String msgSuffix) throws VariableException {
		if(!(type1.equals(type2) || type1.equals(BOOLEAN_ID) &&
				(type2.equals(INT_ID) || type2.equals(DOUBLE_ID)) ||
				type1.equals(DOUBLE_ID) && type2.equals(INT_ID))) {
			throw new InvalidTypeException(UNMATCHING_TYPE_MSG + msgSuffix);
		}
	}

	static void checkVariableUsability(Variable var, int lineNumber, String msgSuffix)
			throws VariableException {
		if(!var.isInitialized()) {
			// if trying to use an uninitialized variable
			throw new UninitializedException(UNINITIALIZED_VAR_MSG + msgSuffix);
		}
		if(lineNumber < var.getLine() && !var.isGlobal()) {
			// in case of trying to reach a variable that wasn't defined yet
			throw new VariableException(INVALID_VAR_MSG + msgSuffix);
		}
	}

	static String getType(String text) {
		Matcher matcher = INT_PATTERN.matcher(text);
		if(matcher.matches()) {
			return INT_ID;
		}
		matcher = STRING_PATTERN.matcher(text);
		if(matcher.matches()) {
			return STRING_ID;
		}
		matcher = CHAR_PATTERN.matcher(text);
		if(matcher.matches()) {
			return CHAR_ID;
		}
		matcher = DOUBLE_PATTERN.matcher(text);
		if(matcher.matches()) {
			return DOUBLE_ID;
		}
		matcher = BOOLEAN_PATTERN.matcher(text);
		if(matcher.matches()) {
			return BOOLEAN_ID;
		}
		return null;
	}

}
