package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static String VALID_VARIABLE_NAME_REGEX = "(?:[a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+)";

    private static String VALID_METHOD_NAME_REGEX = "[a-zA-Z]+[a-zA-Z0-9_]*";



    private static String VARIABLE_POSSIBLE_VALUES_REGEX = "(?: *= *(?:\\\"[\\w]\\\"|[0-9]+|(?:true|false)))?"
            ;

    public static String CHECK_TYPES_REGEX = "(?:int|double|String|boolean|char)";

    private static String METHOD_PARAMETER_REGEX = "\\s*" + CHECK_TYPES_REGEX + " " + VALID_VARIABLE_NAME_REGEX;

    private static String VARIABLE_DECLARING_REGEX = CHECK_TYPES_REGEX + " " + VALID_VARIABLE_NAME_REGEX + //TODO import!!
            VARIABLE_POSSIBLE_VALUES_REGEX + ("(?:" + " *, *" + (VALID_VARIABLE_NAME_REGEX +
            VARIABLE_POSSIBLE_VALUES_REGEX ) + ")" + "?" + "\\*");

    private static Pattern VARIABLE_DECLARING_PATTERN = Pattern.compile(VARIABLE_DECLARING_REGEX);


    private static String IF_OR_WHILE_REGEX = "\\s*(?:if|while)\\s*\\((?:\\s*[a-zA-Z]+[a-zA-Z0-9_]*|_+ " +
            "[a-zA-Z0-9_]+|\\s*(?:\\d+|\\d+\\.\\d+))\\s*(?:(?:\\|\\||&&)\\s*(?:[a-zA-Z]+[a-zA-Z0-9_]*|" +
            "_+[a-zA-Z0-9_]+|\\s*(?:\\d+|\\d+\\.\\d+))\\s*)*\\)\\s*\\{";

    private static String METHOD_DECLARING_REGEX = "\\s*void (" + VALID_METHOD_NAME_REGEX + ") *\\( *" +"(?:"+
            METHOD_PARAMETER_REGEX + " *(?:(?: *, *" + METHOD_PARAMETER_REGEX + ")?)*|) *\\)\\{";

    private static Pattern METHOD_DECLARING_PATTERN = Pattern.compile(METHOD_DECLARING_REGEX);


    private static String CONDITION_GROUPING_REGEX = "(?:\\(|\\|\\||\\&\\&)?\\s*(.+?)\\" +
            "s*(?:\\)|\\|\\||\\&\\&)+";


    private static String METHOD_CALL_REGEX_UGLY = "[a-zA-Z]+[a-zA-Z0-9_]*\\s*\\(\\s*(?:(?:\".*?\"|" +
            "\\s*\\d+\\s*|\\s*\\d+\\.\\d+\\s*|\\s*(?:true|false)|'.??'|(?:[a-zA-Z]+[a-zA-Z0-9_]*|" +
            "_+[a-zA-Z0-9_]+))\\s*(?:\\s*,\\s*(?:\".*?\"|\\s*\\d+\\s*|\\s*\\d+\\.\\d+\\s*|\\s*" +
            "(?:true|false)|'.??'|(?:[a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+)))*?|)\\s*\\);$";



    private static String STRING_REGEX = "\".*\"";

    private static String INT_REGEX = "\\d+";

    private static String DOUBLE_REGEX = "\\d+(?:\\.\\d+)?";

    private static String BOOLEAN_REGEX = "\\s*(?:true|false|" + INT_REGEX + "|" +
            DOUBLE_REGEX + ")" ;

    private static boolean matchPattern(String currLine, Pattern pattern) {
        Matcher m = pattern.matcher(currLine);
        return m.matches();
    }

    public static void parseGlobalBlock(Block blockToParse){
        int lineNum = 0;
        HashMap<String, Method> methodsMap = new HashMap<>();
        while (lineNum < blockToParse.getCodeLines().size()){
            Tuple<String, Integer> currLineTuple = blockToParse.getCodeLines().get(lineNum);
            if(matchPattern(currLineTuple.getFirst(), METHOD_DECLARING_PATTERN)){
                Tuple<ArrayList<Variable>, Method> methodTuple = MethodParser.parseMethod(currLineTuple);
                Method methodParsed = methodTuple.getSecond();
                methodsMap.put();
            }
            lineNum ++;
        }
    }



//    private static String METHOD_DECLARING_REGEX_UGLY = " *void [a-zA-Z]+[a-zA-Z0-9_]* *\\( *(?:(?:int|double|String|boolean|char) (?:[a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+) *(?:(?: *, *(?:int|double|String|boolean|char) (?:[a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+))?)*|) *\\)\\{";


    public static void main(String args[]){
//        System.out.println(VARIABLE_DECLARING_REGEX);
        Pattern p = Pattern.compile(BOOLEAN_REGEX);
        Matcher m = p.matcher("hi");
        Pattern variablePossibleValue = Pattern.compile(VARIABLE_POSSIBLE_VALUES_REGEX);
        Matcher variablePossibleValueMatcher = variablePossibleValue.matcher("");
        System.out.println(m.matches());
    }
}
