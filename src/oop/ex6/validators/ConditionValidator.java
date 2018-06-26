package oop.ex6.validators;

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

    private static class ConditionParser {

        public static final String CONDITION_GROUPING_REGEX = "\\s*(?:(?:if|while)\\s*\\(|\\|\\||\\&\\&" +
                ")?\\s*(.+?)\\s*(?:\\)|\\|\\||\\&\\&)+";

        public static final Pattern CONDITION_GROUPING_PATTERN = Pattern.compile(CONDITION_GROUPING_REGEX);

        public static ArrayList<String> parseParameters(String line) {
            ArrayList<String> params = new ArrayList<>();
            Matcher matcher = CONDITION_GROUPING_PATTERN.matcher(line);
            while (matcher.find()) {
                params.add(matcher.group(1));
            }
            return params;
        }
    }

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
            }
            try {
                VariableValidator.compareTypes(expectedType, type,
                        "");
            }
            catch(VariableException e) {
                throw new InvalidConditionException(INVALID_CONDITION_ERROR + msgSuffix);

            }
        }
    }

}
