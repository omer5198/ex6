package oop.ex6.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodCallValidator {
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

	public void valdiateMethodCall(Tuple<String, Integer> line, Block block, )
}
