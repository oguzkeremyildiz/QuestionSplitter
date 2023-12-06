package org.example;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<ArrayList<Integer>> lists = Splitter.split("Archive/Q5");
        for (ArrayList<Integer> list : lists) {
            System.out.println(list);
        }
    }
}