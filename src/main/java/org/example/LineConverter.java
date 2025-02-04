package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LineConverter {

    private static ArrayList<LineType> getTypes(String line) {
        ArrayList<LineType> types = new ArrayList<>();
        if (line.contains("}")) {
            types.add(LineType.CLOSE);
        }
        if (line.contains("else")) {
            if (line.contains("if")) {
                types.add(LineType.ELSE_IF);
            } else {
                types.add(LineType.ELSE);
            }
        } else if (line.contains("if") && line.contains("(")) {
            types.add(LineType.IF);
        } else if (line.contains("for") && line.contains("(")) {
            types.add(LineType.FOR);
        } else if (line.contains("while") && line.contains("(")) {
            types.add(LineType.WHILE);
        } else {
            if (types.isEmpty()) {
                types.add(LineType.STATEMENT);
            }
        }
        return types;
    }

    private static boolean check(ArrayList<Pair<Integer, LineType>> last) {
        Stack<LineType> stack = new Stack<>();
        for (Pair<Integer, LineType> pair : last) {
            if (pair.getValue().equals(LineType.CLOSE)) {
                if (stack.isEmpty()) {
                    return true;
                }
                stack.pop();
            } else if (pair.getValue().equals(LineType.ELSE) || pair.getValue().equals(LineType.ELSE_IF) || pair.getValue().equals(LineType.IF) || pair.getValue().equals(LineType.FOR) || pair.getValue().equals(LineType.WHILE)) {
                stack.add(pair.getValue());
            }
        }
        return !stack.isEmpty();
    }

    public static ArrayList<ArrayList<Pair<Integer, LineType>>> convert(File file) throws FileNotFoundException, BracesNotMatchException {
        Scanner source = new Scanner(file);
        int index = 0;
        ArrayList<ArrayList<Pair<String, Integer>>> lines = new ArrayList<>();
        ArrayList<ArrayList<Pair<Integer, LineType>>> types = new ArrayList<>();
        while (source.hasNextLine()) {
            String line = source.nextLine();
            index++;
            do {
                if (line.contains("/** ASSESSMENT")) {
                    lines.add(new ArrayList<>());
                    for (int i = 0; i < 4; i++) {
                        line = source.nextLine();
                        index++;
                    }
                }
            } while (line.contains("/** ASSESSMENT"));
            int check = line.indexOf("//");
            if (check != -1) {
                line = line.substring(0, check);
            }
            if (!lines.isEmpty()) {
                if (!line.isBlank()) {
                    lines.get(lines.size() - 1).add(new Pair<>(line, index));
                }
            }
        }
        source.close();
        for (int k = 0; k < lines.size(); k++) {
            ArrayList<Pair<String, Integer>> line = lines.get(k);
            types.add(new ArrayList<>());
            for (int j = 0; j < line.size(); j++) {
                ArrayList<LineType> lineTypes = getTypes(line.get(j).getKey());
                int i = 0;
                if (lineTypes.size() > 1) {
                    i++;
                }
                for (LineType lineType : lineTypes) {
                    types.get(types.size() - 1).add(new Pair<>(line.get(j).getValue(), lineType));
                }
                if (lineTypes.get(i).equals(LineType.FOR) || lineTypes.get(i).equals(LineType.IF) || lineTypes.get(i).equals(LineType.WHILE) || lineTypes.get(i).equals(LineType.ELSE_IF) || lineTypes.get(i).equals(LineType.ELSE)) {
                    if (j + 1 != line.size() && line.get(j + 1).getKey().trim().equals("{")) {
                        j++;
                    } else {
                        if (!line.get(j).getKey().contains("{")) {
                            if (!line.get(j).getKey().contains(";")) {
                                j++;
                            }
                            types.get(types.size() - 1).add(new Pair<>(line.get(j).getValue(), LineType.STATEMENT));
                            types.get(types.size() - 1).add(new Pair<>(line.get(j).getValue(), LineType.CLOSE));
                        }
                    }
                }
            }
            if (k + 1 != lines.size()) {
                if (check(types.get(types.size() - 1))) {
                    throw new BracesNotMatchException();
                }
            }
        }
        if (check(types.get(types.size() - 1))) {
            types.get(types.size() - 1).remove(types.get(types.size() - 1).get(types.get(types.size() - 1).size() - 1));
        }
        return types;
    }
}
