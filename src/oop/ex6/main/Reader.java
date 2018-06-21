package oop.ex6.main;

import java.io.BufferedReader;
import java.io.File;

public class Reader {

    protected BufferedReader br;

    protected File file;

    public Reader(File file){
        this.file = file;
//        this.br = new BufferedReader(file)
    }

}
