package oop.ex6.main;

import oop.ex6.exceptions.condition.ConditionException;
import oop.ex6.exceptions.condition.ConditionInOuterScopeError;
import oop.ex6.exceptions.condition.InvalidConditionException;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Sjavac {

	private static void validateFile(String filePath) throws FileNotFoundException, IOException,
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
		String filePath = args[0];
		try{
			validateFile(filePath);
			System.out.println(0);
		} catch (IOException e) {
			System.out.println(2);
		} catch (Exception e) {
			System.out.println(1);
		}

//		} catch (InvalidMethodException e){
//			System.out.println(1 + e.getMessage());
//		} catch (InvalidBracketsException e){
//			System.out.println(1 + e.getMessage());
//		} catch (VariableException e){
//			System.out.println(1 + e.getMessage());
//		} catch (InvalidConditionException){
	}


}
