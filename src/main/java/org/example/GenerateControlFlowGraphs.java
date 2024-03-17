package org.example;

import java.io.*;
import java.util.*;

public class GenerateControlFlowGraphs {

    private static int addNode(ArrayList<String> codeLines, int j, ControlFlowGraph graph, HashSet<String> nodes) {
        String node = codeLines.get(j);
        for (String cur : nodes) {
            graph.put(cur, node + "-" + j);
        }
        nodes.clear();
        HashSet<Integer> types = Parser.types(codeLines.get(j));
        if (types.isEmpty()) {
            nodes.add(node + "-" + j);
        } else {
            Pair<Integer, HashSet<String>> pair = generateGraph(graph, codeLines, j);
            j = pair.getKey();
            nodes.addAll(pair.getValue());
        }
        return j;
    }

    private static String findHead(ArrayList<String> codeLines, HashSet<Integer> headTypes, ControlFlowGraph graph, int headIndex) {
        String head;
        if (headTypes.contains(2)) {
            head = graph.get();
        } else {
            head = codeLines.get(headIndex) + "-" + headIndex;
            if (headTypes.contains(1)) {
                graph.add(head);
            }
        }
        return head;
    }

    private static void addGraph(ControlFlowGraph graph, HashSet<Integer> headTypes, String head, HashSet<String> nodes) {
        if (headTypes.contains(1) && headTypes.size() == 1) {
            if (!graph.isJustElse()) {
                nodes.add(head);
            } else {
                graph.change();
            }
            graph.pop();
        } else if (headTypes.contains(3)) {
            for (String cur : nodes) {
                graph.put(cur, head);
            }
            nodes.clear();
            nodes.add(head);
        } else if (headTypes.contains(4)) {
            for (String cur : nodes) {
                graph.put(cur, head);
            }
        }
    }

    private static Pair<Integer, HashSet<String>> generateGraph(ControlFlowGraph graph, ArrayList<String> codeLines, int j) {
        HashSet<Integer> headTypes = Parser.types(codeLines.get(j));
        HashSet<String> nodes = new HashSet<>();
        HashSet<Integer> types;
        String head = findHead(codeLines, headTypes, graph, j);
        nodes.add(head);
        j++;
        do {
            j = addNode(codeLines, j, graph, nodes) + 1;
            types = Parser.types(codeLines.get(j));
        } while (!types.contains(0));
        int check = j;
        if (types.size() == 1) {
            check++;
        }
        if (codeLines.size() > check) {
            HashSet<Integer> checkTypes = Parser.types(codeLines.get(check));
            if (checkTypes.contains(2)) {
                if (!checkTypes.contains(1)) {
                    graph.change();
                }
                Pair<Integer, HashSet<String>> pair = generateGraph(graph, codeLines, check);
                j = pair.getKey();
                nodes.addAll(pair.getValue());
            }
        }
        addGraph(graph, headTypes, head, nodes);
        return new Pair<>(j, nodes);
    }

    private static ArrayList<String> getStrings(File listOfFiles) throws FileNotFoundException, BracesNotMatchException {
        int open = 0, close = 0;
        ArrayList<String> codeLines = new ArrayList<>();
        Scanner source = new Scanner(listOfFiles);
        while (source.hasNext()) {
            String line = source.nextLine();
            codeLines.add(line);
            if (Parser.isOpen(line)) {
                open++;
            }
            if (Parser.isClose(line)) {
                close++;
            }
        }
        source.close();
        if (open != close) {
            throw new BracesNotMatchException(codeLines);
        }
        return codeLines;
    }

    public static ArrayList<ControlFlowGraph> generateGraphs(String folderName) throws FileNotFoundException, BracesNotMatchException {
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        ArrayList<ControlFlowGraph> graphs = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].getName().equals("RefCode.java")) {
                ArrayList<String> codeLines = getStrings(listOfFiles[i]);
                ControlFlowGraph graph = new ControlFlowGraph();
                HashSet<String> nodes = new HashSet<>();
                nodes.add(codeLines.get(0) + "-0");
                int j = 1;
                if (codeLines.get(j).trim().equals("{")) {
                    j++;
                }
                while (j < codeLines.size() - 1) {
                    j = addNode(codeLines, j, graph, nodes) + 1;
                }
                graphs.add(graph);
                System.out.println(listOfFiles[i].getName() + " is done.");
                //System.out.println(graph);
            }
        }
        return graphs;
    }
}
