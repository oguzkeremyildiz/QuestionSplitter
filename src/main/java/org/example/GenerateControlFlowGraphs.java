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

    private static Pair<Integer, HashSet<ControlFlowNode>> ifOperator(ControlFlowGraph graph, ArrayList<Pair<Statement, Integer>> commands, int j) {
        ControlFlowNode ifNode = new ControlFlowNode(commands.get(j).getValue());
        HashSet<ControlFlowNode> nodes = new HashSet<>();
        do {
            j++;
            Pair<Statement, Integer> command = commands.get(j);
            ControlFlowNode node = new ControlFlowNode(command.getValue());
            //graph.put(node);
            if (command.getKey().equals(Statement.IF)) {
                Pair<Integer, HashSet<ControlFlowNode>> p = ifOperator(graph, commands, j);
                j = p.getKey();
            } else if (command.getKey().equals(Statement.WHILE)) {

            } else if (command.getKey().equals(Statement.FOR)) {

            } else {

            }
        } while (!commands.get(j).getKey().equals(Statement.CLOSE));
        j++;
        if (commands.get(j).getKey().equals(Statement.ELSE)) {
            
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
                    //graph.addLeaf(new ControlFlowNode(0));
                    for (int j = 0; j < commands.size(); j++) {
                        Pair<Statement, Integer> command = commands.get(i);
                        if (command.getKey().equals(Statement.NORMAL)) {
                            ControlFlowNode node = new ControlFlowNode(command.getValue());
                           // graph.put(node);
                            //graph.setLast(node);
                        } else if (command.getKey().equals(Statement.IF)) {
                           // j = ifOperator(graph, commands, j);
                        }
                    }
                    graphs.add(graph);
                    System.out.println(listOfFiles[i].getName() + " is done.");
                } catch (BracesNotMatchException e) {
                    System.out.println(listOfFiles[i].getName() + " is not done.");
                }
            }
        }
        return graphs;
    }
}
