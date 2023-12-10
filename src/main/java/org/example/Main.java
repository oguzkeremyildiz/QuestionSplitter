package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, CloneNotSupportedException {
        String question = "Archive/Q1";
        ArrayList<ArrayList<Integer>> lists = CodeAssessment.calculateBestSplits(question, new LineCalculator(new File(question + "/refCode.java")));
        for (ArrayList<Integer> list : lists) {
            System.out.println(list);
        }
    }
}