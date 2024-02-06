package org.example;

import java.io.*;
import java.util.*;

public class GenerateControlFlowGraphs {

    private static ArrayList<Pair<Statement, Integer>> createCommands(ArrayList<String> lines) throws BracesNotMatchException {
        ArrayList<Pair<Statement, Integer>> commands = new ArrayList<>();
        int open = 0, close = 0;
        int i = 1;
        if (lines.get(i).trim().equals("{")) {
            i++;
        }
        while (i < lines.size() - 1) {
            if (lines.get(i).contains("}")) {
                close++;
                commands.add(new Pair<>(Statement.CLOSE, i));
            }
            if (lines.get(i).contains("while")) {
                open++;
                commands.add(new Pair<>(Statement.WHILE, i));
            } else if (lines.get(i).contains("else")) {
                open++;
                commands.add(new Pair<>(Statement.ELSE, i));
            } else if (lines.get(i).contains("if")) {
                open++;
                commands.add(new Pair<>(Statement.IF, i));
            } else {
                if (!lines.get(i).isEmpty()) {
                    commands.add(new Pair<>(Statement.NORMAL, i));
                }
            }
            i++;
        }
        if (open != close) {
            throw new BracesNotMatchException(lines);
        }
        return commands;
    }

    private static Pair<Integer, HashSet<Integer>> generateGraph(ControlFlowGraph graph, ArrayList<Pair<Statement, Integer>> commands, int j) {
        Statement statement = commands.get(j).getKey();
        int head = commands.get(j).getValue();
        HashSet<Integer> nodes = new HashSet<>();
        nodes.add(head);
        j++;
        do {
            Pair<Statement, Integer> command = commands.get(j);
            int node = command.getValue();
            for (int cur : nodes) {
                graph.put(cur, node);
            }
            nodes.clear();
            if (command.getKey().equals(Statement.NORMAL)) {
                nodes.add(node);
            } else {
                Pair<Integer, HashSet<Integer>> pair = generateGraph(graph, commands, j);
                j = pair.getKey();
                nodes.addAll(pair.getValue());
            }
            j++;
        } while (!commands.get(j).getKey().equals(Statement.CLOSE));
        if (commands.size() > j + 1 && commands.get(j + 1).getKey().equals(Statement.ELSE)) {
            graph.put(head, commands.get(j + 1).getValue());
            Pair<Integer, HashSet<Integer>> pair = generateGraph(graph, commands, j + 1);
            j = pair.getKey();
            nodes.addAll(pair.getValue());
        } else {
            switch (statement) {
                case IF:
                    nodes.add(head);
                    break;
                case WHILE:
                    for (Integer cur : nodes) {
                        graph.put(cur, head);
                    }
                    nodes.clear();
                    nodes.add(head);
                    break;
                case FOR:
                    for (Integer cur : nodes) {
                        graph.put(cur, head);
                    }
                    break;
            }
        }
        return new Pair<>(j, nodes);
    }

    public static ArrayList<ControlFlowGraph> generateGraphs(String folderName) throws FileNotFoundException {
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        ArrayList<ControlFlowGraph> graphs = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].getName().equals("RefCode.java")) {
                ArrayList<String> codeLines = new ArrayList<>();
                Scanner source = new Scanner(listOfFiles[i]);
                while (source.hasNext()) {
                    codeLines.add(source.nextLine());
                }
                source.close();
                try {
                    ArrayList<Pair<Statement, Integer>> commands = createCommands(codeLines);
                    ControlFlowGraph graph = new ControlFlowGraph();
                    HashSet<Integer> nodes = new HashSet<>();
                    nodes.add(0);
                    for (int j = 0; j < commands.size(); j++) {
                        Pair<Statement, Integer> command = commands.get(j);
                        int node = command.getValue();
                        for (int cur : nodes) {
                            graph.put(cur, node);
                        }
                        nodes.clear();
                        if (command.getKey().equals(Statement.NORMAL)) {
                            nodes.add(node);
                        } else {
                            Pair<Integer, HashSet<Integer>> pair = generateGraph(graph, commands, j);
                            j = pair.getKey();
                            nodes.addAll(pair.getValue());
                        }
                    }
                    graphs.add(graph);
                    System.out.println(listOfFiles[i].getName() + " is done.");
                    //System.out.println(graph);
                } catch (BracesNotMatchException e) {
                    System.out.println(listOfFiles[i].getName() + " is not done.");
                }
            }
        }
        return graphs;
    }
}
