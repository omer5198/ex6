package oop.ex6.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Sjavac {

	private static void validateFile(String filePath) throws FileNotFoundException, IOException,
			InvalidBracketsException, MethodInInnerScopeException, VariableException, InvalidLineException,
	InvalidConditionException, InvalidMethodException, ConditionInOuterScopeError,
			ParameterException, MethodCallInOuterScopeException{
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
