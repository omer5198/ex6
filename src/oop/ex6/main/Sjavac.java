package oop.ex6.main;

import oop.ex6.exceptions.condition.ConditionException;
import oop.ex6.exceptions.method.MethodException;
import oop.ex6.exceptions.parsing.InvalidBracketsException;
import oop.ex6.exceptions.parsing.InvalidLineException;
import oop.ex6.exceptions.variable.VariableException;
import oop.ex6.file.analysis.Reader;
import oop.ex6.main.parsing.Block;
import oop.ex6.main.parsing.InitialScanner;
import oop.ex6.main.parsing.Parser;
import oop.ex6.tools.Tuple;
import oop.ex6.validators.Method;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Receives an s-Java file and validates it
 */
public class Sjavac {

	private static final int VALID = 0;

	private static final int INVALID = 1;

	private static final int IO_EXCEPTION = 2;

	private static final String INVALID_ARGS_ERROR = "Invalid number of arguments provided";

	private static void validateFile(String filePath) throws IOException,
			InvalidBracketsException, VariableException, InvalidLineException,
			ConditionException, MethodException {
		Reader fileReader = new Reader(filePath);
		ArrayList<String> listOfLines = fileReader.readFile();
		InitialScanner initialScanner = new InitialScanner(listOfLines);
		Tuple<Block, ArrayList<Block>> blocksOfFile = initialScanner.initialParse();
		HashMap<String, Method> methodsMap = Parser.parseGlobalBlock(blocksOfFile.getFirst());
		for (Block block: blocksOfFile.getSecond()){
			Parser.parseBlock(block, methodsMap);
		}
	}

	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println(IO_EXCEPTION);
			System.err.println(INVALID_ARGS_ERROR);
			return;
		}
		String filePath = args[0];
		try{
			validateFile(filePath);
			System.out.println(VALID);
		} catch (IOException e) {
			System.out.println(IO_EXCEPTION);
			System.err.println(e.getMessage());
		} catch (VariableException | MethodException | ConditionException |
				InvalidLineException | InvalidBracketsException e) {
			System.out.println(INVALID);
			System.err.println(e.getMessage());
		}
	}
}
