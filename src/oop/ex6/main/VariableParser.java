package oop.ex6.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VariableParser {

	final static private String VAR_SPLIT_REGEX =
			"\\b((?:_[A-Za-z_\\d]+|[A-Za-z]+[A-Za-z_\\d]*))(?:\\s*\\=\\s*(.+?))?\\s*[,;]";

	final static private Pattern VAR_SPLIT_PATTERN = Pattern.compile(VAR_SPLIT_REGEX);

	public static Variable[] parseVariable(String line) {
		parseVariable(line, null);
	}

	public static ArrayList<Tuple<String, String>> parseVariable(String line, String type) {
		ArrayList<Tuple<String, String>> variables = new ArrayList<>();
		Matcher matcher = VAR_SPLIT_PATTERN.matcher(line);
		while(matcher.find()) {
			String name = matcher.group(1);
			String value = matcher.group(2);
			if(type != null) {
				variables.add(new Tuple(name, value));
			}
			else {

			}
		}
	}
}
