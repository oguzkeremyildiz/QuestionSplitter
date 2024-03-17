package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GenerateAbstractSyntaxTrees {

    private static String construct(String parent, Graph graph, String line, int j) {
        String fullLine = line + "-" + j;
        String body = fullLine;
        String condition;
        if (line.contains("if")) {
            condition = line.substring(line.indexOf("(") + 1, line.indexOf(")")) + "-" + j;
            graph.put(parent, condition);
            graph.put(parent, fullLine);
        } else if (line.contains("else")) {
            graph.put(parent, fullLine);
        } else if (line.contains("while") || line.contains("for")) {
            condition = line.substring(line.indexOf("(") + 1, line.indexOf(")"))  + "-" + j;
            graph.put(parent, fullLine);
            graph.put(fullLine, condition);
            body += "-branch";
            graph.put(fullLine, body);
        }
        return body;
    }

    private static int solve(int j, ArrayList<String> lines, Graph graph, String parent) {
        String body = construct(parent, graph, lines.get(j), j);
        j++;
        while (!lines.get(j).trim().equals("}")) {
            String cur = lines.get(j);
            HashSet<Integer> curTypes = Parser.types(cur);
            if (isNormal(curTypes)) {
                graph.put(body, cur + "-" + j);
            } else if (curTypes.contains(2)) {
                j = solve(j, lines, graph, parent);
                break;
            } else {
                j = solve(j, lines, graph, body);
            }
            j++;
        }
        return j;
    }

    private static boolean isNormal(HashSet<Integer> types) {
        return types.isEmpty();
    }

    public static ArrayList<Graph> generateGraphs(String folderName) throws FileNotFoundException, BracesNotMatchException {
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        ArrayList<Graph> graphs = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].getName().equals("RefCode.java")) {
                ArrayList<String> codeLines = new ArrayList<>();
                Scanner source = new Scanner(listOfFiles[i]);
                int open = 0, close = 0;
                while (source.hasNext()) {
                    String cur = source.nextLine();
                    codeLines.add(cur);
                    if (cur.contains("{")) {
                        open++;
                    }
                    if (cur.contains("}")) {
                        close++;
                    }
                }
                source.close();
                if (open != close) {
                    throw new BracesNotMatchException(codeLines);
                }
                Graph graph = new Graph();
                String parent = codeLines.get(0) + "-" + 0;
                for (int j = 1; j < codeLines.size() - 1; j++) {
                    String line = codeLines.get(j);
                    HashSet<Integer> lineTypes = Parser.types(line);
                    if (isNormal(lineTypes)) {
                        graph.put(parent, line + "-" + j);
                    } else {
                        j = solve(j, codeLines, graph, parent);
                    }
                }
                graphs.add(graph);
                System.out.println(listOfFiles[i].getName() + " is done.");
                //System.out.println(graph);
            }
        }
        return graphs;
    }
}
