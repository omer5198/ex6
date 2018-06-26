package oop.ex6.validators;

/*
 * This class validates a condition line in the code. i.e validates an if\while statement within the code
 * (under the assumption that the line as a whole is valid)
 */


import oop.ex6.exceptions.condition.InvalidConditionException;
import oop.ex6.exceptions.variable.UninitializedException;
import oop.ex6.exceptions.variable.VariableException;
import oop.ex6.validators.Variable;
import oop.ex6.main.parsing.Block;
import oop.ex6.tools.Tuple;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionValidator {

	final static private String INVALID_CONDITION_ERROR = "Invalid condition";

	final static private String VAR_NOT_EXIST_ERROR = "Tried to use an uninitialized variable in a condition";

    private static class ConditionParser {

        // a regex that groups the parameters of an if\while statement
		private static final String CONDITION_GROUPING_REGEX = "\\s*(?:(?:if|while)\\s*\\(|\\|\\||&&" +
                ")?\\s*(.+?)\\s*(?:\\)|\\|\\||&&)+";

        // the pattern of the above regex
		private static final Pattern CONDITION_GROUPING_PATTERN = Pattern.compile(CONDITION_GROUPING_REGEX);

        /**
         * This method returns all the parameters inside an if\while statement given a line of such.
         * @param line The text of the if\while statement line.
         * @return A list with the parameter names in the condition statement.
         */
        static ArrayList<String> parseParameters(String line) {
            ArrayList<String> params = new ArrayList<>();
            Matcher matcher = CONDITION_GROUPING_PATTERN.matcher(line);
            while (matcher.find()) {
                params.add(matcher.group(1));
            }
            return params;
        }
    }

    /**
     * This method receives a line as a tuple (also containing the number of line in the code) in which
     * there is an if\while statement. It returns true iff the line is valid in the code according to the
     * javas specifications.
     * @param lineTuple The line tuple received
     * @param block The block in which the line is written
     * @throws InvalidConditionException when condition is invalid.
     * @throws VariableException if there are variables used which cause an error.
     */
    public static void validateCondition(Tuple<String, Integer> lineTuple, Block block) throws
            InvalidConditionException, VariableException {

        ArrayList<String> conditions = ConditionParser.parseParameters(lineTuple.getFirst());
        String expectedType = VariableValidator.getType("true");
        int lineNumber = lineTuple.getSecond();
		String msgSuffix = " | Line " + String.valueOf(lineNumber);
        for(String condition: conditions){
            String type;
			Variable variableForCondition = block.getVariable(condition);
            if (variableForCondition != null){
            	if(!variableForCondition.isInitialized()) {
            		throw new UninitializedException(VariableValidator.UNINITIALIZED_VAR_MSG + msgSuffix);
				}
				if(lineNumber < variableForCondition.getLine() && !variableForCondition.isGlobal()) {
					// in case of trying to reach a variable that wasn't defined yet
					throw new VariableException(VariableValidator.INVALID_VAR_MSG + msgSuffix);
				}
                type = variableForCondition.getType();
            }
            else{
                type = VariableValidator.getType(condition);
                if(type == null) {
                	throw new InvalidConditionException(VAR_NOT_EXIST_ERROR + msgSuffix);
				}
            }
            try {
            	if(expectedType == null) {
					throw new InvalidConditionException(INVALID_CONDITION_ERROR + msgSuffix);
				}
                VariableValidator.compareTypes(expectedType, type, "");
            }
            catch(VariableException e) {
                throw new InvalidConditionException(INVALID_CONDITION_ERROR + msgSuffix);
            }
        }
    }

}
