package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class CodeAssessment {

    private static int findSplitSize(File file) throws FileNotFoundException {
        int size = 0;
        Scanner source = new Scanner(file);
        while (source.hasNext()) {
            String line = source.nextLine();
            if (line.contains("ASSESSMENT")) {
                size++;
            }
        }
        source.close();
        return size;
    }

    private static ArrayList<Integer> findCandidates(ArrayList<Integer> possibilities, ArrayList<Integer> splits) {
        ArrayList<Integer> candidates = new ArrayList<>();
        int i = 0;
        if (!splits.isEmpty()) {
            i = splits.get(splits.size() - 1) + 1;
        }
        for (; i < possibilities.size(); i++) {
            candidates.add(i);
        }
        return candidates;
    }

    private static Pair<ArrayList<Integer>, Double> backtrack(int splitSize, ArrayList<Integer> splits, PointCalculator calculator, ArrayList<Integer> possibilities) throws CloneNotSupportedException {
        ArrayList<Integer> candidates = findCandidates(possibilities, splits);
        if (candidates.isEmpty() || splitSize == splits.size()) {
            return new Pair<>((ArrayList<Integer>) splits.clone(), calculator.calculate());
        }
        Pair<ArrayList<Integer>, Double> pair = new Pair<>(null, Double.MIN_VALUE);
        for (Integer candidate : candidates) {
            splits.add(candidate);
            Pair<ArrayList<Integer>, Double> p = backtrack(splitSize, splits, calculator, possibilities);
            if (pair.getValue() < p.getValue()) {
                pair = p.clone();
            }
            splits.remove(splits.size() - 1);
        }
        return pair;
    }

    public static ArrayList<ArrayList<Integer>> calculateBestSplits(String folderName) throws FileNotFoundException, CloneNotSupportedException {
        int splitSize = findSplitSize(new File(folderName + "/RefCode.java"));
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        ArrayList<ArrayList<Integer>> splitLines = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile()) {
                splitLines.add(backtrack(splitSize, new ArrayList<>(), new DummyCalculator(), Splitter.split(listOfFiles[i])).getKey());
            }
        }
        return splitLines;
    }
}
