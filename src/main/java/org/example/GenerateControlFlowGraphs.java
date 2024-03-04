package org.example;

import java.io.*;
import java.util.*;

public class GenerateControlFlowGraphs {

    private static int addNode(ArrayList<String> codeLines, int j, ControlFlowGraph graph, HashSet<String> nodes) {
        String node = codeLines.get(j);
        for (String cur : nodes) {
            graph.put(cur, node);
        }
        nodes.clear();
        HashSet<Integer> types = Parser.types(codeLines.get(j));
        if (types.isEmpty()) {
            nodes.add(node);
        } else {
            Pair<Integer, HashSet<String>> pair = generateGraph(graph, codeLines, j);
            j = pair.getKey();
            nodes.addAll(pair.getValue());
        }
        return j;
    }

    private static Pair<Integer, HashSet<String>> generateGraph(ControlFlowGraph graph, ArrayList<String> codeLines, int j) {
        HashSet<Integer> types = Parser.types(codeLines.get(j));
        String head = codeLines.get(j);
        if (types.contains(1)) {
            graph.add(head);
        }
        HashSet<String> nodes = new HashSet<>();
        nodes.add(head);
        j++;
        do {
            j = addNode(codeLines, j, graph, nodes) + 1;
        } while (!Parser.types(codeLines.get(j)).contains(0));
        if (codeLines.size() > j + 1 && Parser.types(codeLines.get(j + 1)).contains(2)) {
            graph.put(graph.get(), codeLines.get(j + 1));
            Pair<Integer, HashSet<String>> pair = generateGraph(graph, codeLines, j + 1);
            j = pair.getKey();
            nodes.addAll(pair.getValue());
        }
        if (types.contains(1)) {
            nodes.add(head);
            graph.pop();
        } else if (types.contains(3)) {
            for (String cur : nodes) {
                graph.put(cur, head);
            }
            nodes.clear();
            nodes.add(head);
        } else if (types.contains(4)) {
            for (String cur : nodes) {
                graph.put(cur, head);
            }
        }
        return new Pair<>(j, nodes);
    }

    public static ArrayList<ControlFlowGraph> generateGraphs(String folderName) throws FileNotFoundException, BracesNotMatchException {
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        ArrayList<ControlFlowGraph> graphs = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].getName().equals("RefCode.java")) {
                int open = 0, close = 0;
                ArrayList<String> codeLines = new ArrayList<>();
                Scanner source = new Scanner(listOfFiles[i]);
                while (source.hasNext()) {
                    String line = source.nextLine();
                    codeLines.add(line);
                    HashSet<Integer> types = Parser.types(line);
                    if (types.contains(0)) {
                        close++;
                        if (types.size() > 1) {
                            open++;
                        }
                    } else {
                        if (!types.isEmpty()) {
                            open++;
                        }
                    }
                }
                source.close();
                if (open != close) {
                    throw new BracesNotMatchException(codeLines);
                }
                ControlFlowGraph graph = new ControlFlowGraph();
                HashSet<String> nodes = new HashSet<>();
                nodes.add(codeLines.get(0));
                int j = 1;
                if (codeLines.get(j).trim().equals("{")) {
                    j++;
                }
                while (j < codeLines.size()) {
                    j = addNode(codeLines, j, graph, nodes) + 1;
                }
                graphs.add(graph);
                System.out.println(listOfFiles[i].getName() + " is done.");
                System.out.println(graph);
            }
        }
        return graphs;
    }
}
