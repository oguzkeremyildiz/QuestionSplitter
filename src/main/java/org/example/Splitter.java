package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Splitter {

    private static ArrayList<Pair<Command, Integer>> createCommands(ArrayList<String> lines) {
        ArrayList<Pair<Command, Integer>> commands = new ArrayList<>();
        for (int i = 1; i < lines.size() - 1; i++) {
            if ((lines.get(i).contains("while") || lines.get(i).contains("if") || lines.get(i).contains("for")) && lines.get(i).contains("{")) {
                commands.add(new Pair<>(Command.OPEN, i));
            } else if (lines.get(i).contains("}")) {
                commands.add(new Pair<>(Command.CLOSE, i));
            } else {
                if (!lines.get(i).isEmpty()) {
                    commands.add(new Pair<>(Command.STATEMENT, i));
                }
            }
        }
        return commands;
    }

    private static Pair<Integer, Integer> solve(int curIndex, ArrayList<Pair<Command, Integer>> commands, ArrayList<Integer> splitLines) {
        ArrayList<Integer> tmp = new ArrayList<>();
        for (int i = curIndex + 1; i < commands.size(); i++) {
            if (commands.get(i).getKey().equals(Command.CLOSE)) {
                if (tmp.size() > 1) {
                    for (Integer integer : tmp) {
                        if (integer != -1) {
                            splitLines.add(integer);
                        }
                    }
                    return new Pair<>(commands.get(curIndex).getValue(), i);
                }
                return new Pair<>(-1, i);
            } else if (commands.get(i).getKey().equals(Command.OPEN)) {
                Pair<Integer, Integer> entry = solve(i, commands, splitLines);
                tmp.add(entry.getKey());
                i = entry.getValue();
            } else {
                tmp.add(commands.get(i).getValue());
            }
        }
        return new Pair<>(-1, -1);
    }

    public static ArrayList<Integer> split(File file) throws FileNotFoundException {
        ArrayList<Integer> splitLines = new ArrayList<>();
        splitLines.add(0);
        Scanner source = new Scanner(file);
        ArrayList<String> lines = new ArrayList<>();
        while (source.hasNext()) {
            String line = source.nextLine();
            lines.add(line);
        }
        source.close();
        ArrayList<Pair<Command, Integer>> commands = createCommands(lines);
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).getKey().equals(Command.OPEN)) {
                Pair<Integer, Integer> sol = solve(i, commands, splitLines);
                if (sol.getKey() != -1) {
                    splitLines.add(commands.get(i).getValue());
                }
                i = sol.getValue();
            } else {
                splitLines.add(commands.get(i).getValue());
            }
        }
        Collections.sort(splitLines);
        return splitLines;
    }

    public static ArrayList<ArrayList<Integer>> split(String folderName) throws FileNotFoundException {
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        ArrayList<ArrayList<Integer>> splitLines = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile()) {
                splitLines.add(split(listOfFiles[i]));
            }
        }
        return splitLines;
    }
}
