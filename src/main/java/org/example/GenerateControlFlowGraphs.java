package org.example;

import java.io.*;
import java.util.*;

public class GenerateControlFlowGraphs {

    private static boolean condition(ArrayList<Pair<Integer, LineType>> line, ArrayList<String> lasts, String prev) {
        if (line.get(0).getValue().equals(LineType.CLOSE)) {
            lasts.add(prev);
            return line.size() > 1 && (line.get(1).getValue().equals(LineType.ELSE_IF) || line.get(1).getValue().equals(LineType.ELSE));
        }
        return true;
    }

    private static String ifStatement(String prev, Graph graph, ArrayList<Pair<Integer, LineType>> line) {
        String first = prev;
        ArrayList<String> lasts = new ArrayList<>();
        boolean isThereAnElse = false;
        while (condition(line, lasts, prev)) {
            if (line.get(0).getValue().equals(LineType.CLOSE)) {
                prev = first;
                line.remove(0);
                if (line.get(0).getValue().equals(LineType.ELSE)) {
                    isThereAnElse = true;
                }
                line.remove(0);
            }
            if (!line.get(0).getValue().equals(LineType.CLOSE)) {
                prev = addNode(prev, graph, line);
            }
        }
        line.remove(0);
        String end = "end-" + first;
        for (String last : lasts) {
            graph.put(last, end);
        }
        if (!isThereAnElse) {
            graph.put(first, end);
        }
        return end;
    }

    private static String forStatement(String prev, Graph graph, ArrayList<Pair<Integer, LineType>> line) {
        String first = prev;
        while (!line.get(0).getValue().equals(LineType.CLOSE)) {
            prev = addNode(prev, graph, line);
        }
        line.remove(0);
        graph.put(prev, first);
        return prev;
    }

    private static String whileStatement(String prev, Graph graph, ArrayList<Pair<Integer, LineType>> line) {
        String first = prev;
        while (!line.get(0).getValue().equals(LineType.CLOSE)) {
            prev = addNode(prev, graph, line);
        }
        line.remove(0);
        graph.put(prev, first);
        return first;
    }
    
    private static String addNode(String prev, Graph graph, ArrayList<Pair<Integer, LineType>> line) {
        Pair<Integer, LineType> pair = line.remove(0);
        String cur;
        switch (pair.getValue()) {
            case IF:
                cur = "if-" + pair.getKey();
                graph.put(prev, cur);
                prev = cur;
                return ifStatement(prev, graph, line);
            case STATEMENT:
                cur = "statement-" + pair.getKey();
                graph.put(prev, cur);
                return cur;
            case FOR:
                cur = "for-" + pair.getKey();
                graph.put(prev, cur);
                prev = cur;
                return forStatement(prev, graph, line);
            case WHILE:
                cur = "while-" + pair.getKey();
                graph.put(prev, cur);
                prev = cur;
                return whileStatement(prev, graph, line);
        }
        return "null";
    }

    public static ArrayList<ArrayList<Graph>> generateGraphs(String folderName) throws FileNotFoundException {
        File folder = new File(folderName);
        return generateGraphs(folder);
    }

    public static ArrayList<ArrayList<Graph>> generateGraphs(File folder) throws FileNotFoundException {
        File[] listOfFiles = folder.listFiles();
        ArrayList<ArrayList<Graph>> graphs = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].getName().equals("RefCode.java") && !listOfFiles[i].getName().contains(".DS_Store")) {
                File file = listOfFiles[i];
                //System.out.println(file.getName());
                try {
                    ArrayList<ArrayList<Pair<Integer, LineType>>> lines = LineConverter.convert(file);
                    ArrayList<Graph> graph = new ArrayList<>();
                    for (ArrayList<Pair<Integer, LineType>> line : lines) {
                        Graph current = new Graph();
                        String prev = "start-0";
                        while (!line.isEmpty()) {
                            prev = addNode(prev, current, line);
                        }
                        graph.add(current.clone());
                    }
                    graphs.add((ArrayList<Graph>) graph.clone());
                } catch (BracesNotMatchException exception) {
                    System.out.println(file.getName() + " not done.");
                }
            }
        }
        return graphs;
    }
}
