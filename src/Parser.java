import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static String VALID_VARIABLE_NAME_REGEX = "(?:[a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+)";

    private static String VALID_METHOD_NAME_REGEX = "[a-zA-Z]+[a-zA-Z0-9_]*";



    private static String VARIABLE_POSSIBLE_VALUES_REGEX = "(?: *= *(?:\\\"[\\w]\\\"|[0-9]+|(?:true|false)))?"
            ;

    private static String CHECK_TYPES_REGEX = " *(?:int|double|String|boolean|char)";

    private static String METHOD_PARAMETER_REGEX = CHECK_TYPES_REGEX + " " + VALID_VARIABLE_NAME_REGEX;

    private static String VARIABLE_DECLARING_REGEX = CHECK_TYPES_REGEX + " " + VALID_VARIABLE_NAME_REGEX +
            VARIABLE_POSSIBLE_VALUES_REGEX + ("(?:" + " *, *" + (VALID_VARIABLE_NAME_REGEX +
            VARIABLE_POSSIBLE_VALUES_REGEX ) + ")" + "?" + "\\*");

    private static String IF_OR_WHILE_REGEX = " *(?:if|while) *\\((?: *[a-zA-Z]+[a-zA-Z0-9_]*|_+" +
            "[a-zA-Z0-9_]+) *(?:(?:(?:\\|\\||&&) *(?:[a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+) *)?)*\\) *\\{";

    private static String METHOD_DECLARING_REGEX = " *void " + VALID_METHOD_NAME_REGEX + " *\\( *" +"(?:"+
            METHOD_PARAMETER_REGEX + " *(?:(?: *, *" + METHOD_PARAMETER_REGEX + ")?)*|) *\\)\\{";

//    private static String METHOD_DECLARING_REGEX_UGLY = " *void [a-zA-Z]+[a-zA-Z0-9_]* *\\( *(?:(?:int|double|String|boolean|char) (?:[a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+) *(?:(?: *, *(?:int|double|String|boolean|char) (?:[a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+))?)*|) *\\)\\{";

    public static void main(String args[]){
//        System.out.println(VARIABLE_DECLARING_REGEX);
        Pattern p = Pattern.compile(METHOD_DECLARING_REGEX);
        Matcher m = p.matcher("void roni(boolean jkj, int a  , char w, String _s){");
        Pattern variablePossibleValue = Pattern.compile(VARIABLE_POSSIBLE_VALUES_REGEX);
        Matcher variablePossibleValueMatcher = variablePossibleValue.matcher("");
        System.out.println(m.matches());
    }
}
