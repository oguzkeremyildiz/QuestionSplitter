package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GenerateAbstractSyntaxTrees {

   /* private static int construct(int parent, Graph graph, String line, int j) {
        int body = -1;
        if (line.contains("if")) {
            graph.put(parent, j * 2134);
            graph.put(parent, j);
            body = j;
        } else if (line.contains("else")) {
            graph.put(parent, j);
            body = j;
        } else if (line.contains("for")) {
            graph.put(parent, j * 3124);
            graph.put(j * 3124, j);
            graph.put(j, j * 2134);
            body = j * 1234;
            graph.put(j, body);
        } else if (line.contains("while")) {
            graph.put(parent, j);
            graph.put(j, j * 2134);
            body = j * 1234;
            graph.put(j, body);
        }
        return body;
    }

    private static int solve(int j, ArrayList<String> lines, Graph graph, int parent) {
        int body = construct(parent, graph, lines.get(j), j);
        j++;
        while (!lines.get(j).contains("}")) {
            String cur = lines.get(j);
            if (isNormal(cur)) {
                graph.put(body, j);
            } else {
                j = solve(j, lines, graph, body);
            }
            j++;
        }
        if (lines.get(j).contains("else")) {
            return j - 1;
        }
        return j;
    }

    private static boolean isNormal(String line) {
        return !line.contains("for") && !line.contains("else") && !line.contains("if") && !line.contains("while");
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
                int parent = 0;
                for (int j = 1; j < codeLines.size() - 1; j++) {
                    String line = codeLines.get(j);
                    if (isNormal(line)) {
                        graph.put(parent, j);
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
    }*/
}
