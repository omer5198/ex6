package oop.ex6.main;

import java.io.*;
import java.util.ArrayList;

/**
 * This class implements a file reader, which will return all lines from a file
 * that we wish to verify
 */

public class Reader {

    private static final String FILE_NOT_FOUND_MSG = "file not found";

    protected BufferedReader br;

    protected File file;

    /**
     * The constructor of the reader.
     * @param file the file that the reader is supposed to read
     * @throws FileNotFoundException Thrown when file is not found
     */
    public Reader(File file) throws FileNotFoundException{
        this.file = file;
        try {
            this.br = new BufferedReader(new FileReader(file));
        }
        catch (FileNotFoundException e){
            throw new FileNotFoundException(FILE_NOT_FOUND_MSG);
        }
    }

    /**
     * This method returns all the lines of a file as an arraylist
     * @return An arraylist of all line strings in the file
     * @throws IOException may be thrown on BufferReader's readline() method
     */
    public ArrayList<String> readFile() throws IOException{

        ArrayList<String> listOfLines = new ArrayList<>();
        String line = br.readLine();


        while(line != null){
            listOfLines.add(line);
            line = br.readLine();
        }
        return listOfLines;
    }



}
