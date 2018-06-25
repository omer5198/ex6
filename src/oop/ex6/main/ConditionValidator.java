package oop.ex6.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionValidator {

    private static final String VALID_VARIABLE_NAME_REGEX = "(?:[a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+)";

    private static final Pattern VALID_VARIABLE_NAME_PATTERN = Pattern.compile(VALID_VARIABLE_NAME_REGEX);

//    private static final String BOOLEAN_REGEX = "\\s*(?:true|false|" + INT_REGEX + "|" +
//            DOUBLE_REGEX + ")" ;

    private static class ConditionParser {

        private static final String CONDITION_GROUPING_REGEX = "(?:\\(|\\|\\||\\&\\&)?\\s*(.+?)\\" +
                "s*(?:\\)|\\|\\||\\&\\&)+";

        private static final Pattern CONDITION_GROUPING_PATTERN = Pattern.compile(CONDITION_GROUPING_REGEX);

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
            InvalidConditionException{

        ArrayList<String> conditions = ConditionParser.parseParameters(lineTuple.getFirst());
        String expectedType = VariableValidator.getType("true");
        for(String condition: conditions){
            String type;
            Variable variableForCondition = block.getVariable(condition);
            if (variableForCondition != null){
                type = variableForCondition.getType();
            } else{
                type = VariableValidator.getType(condition);
            }
            try {
                VariableValidator.compareTypes(expectedType, type,
                        "");
            }
            catch(VariableException e) {
                throw new InvalidConditionException("Invalid condition in line "+ lineTuple.getSecond());

            }
        }
    }

    private static boolean matchSpecificPattern(String text, Pattern pattern){
        Matcher m = pattern.matcher(text);
        return m.matches();
    }

}
