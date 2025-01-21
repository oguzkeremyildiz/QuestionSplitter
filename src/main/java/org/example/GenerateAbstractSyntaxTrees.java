package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GenerateAbstractSyntaxTrees {

    private static String construct(String parent, Graph graph, String line, int j, LineType lineType) {
        String fullLine = line + "-" + j;
        String body = fullLine;
        String condition;
        if (lineType.equals(LineType.IF) || lineType.equals(LineType.ELSE_IF)) {
            condition = line.substring(line.indexOf("(") + 1, line.indexOf(")")) + "-" + j;
            graph.put(parent, condition);
            graph.put(parent, fullLine);
        } else if (lineType.equals(LineType.ELSE)) {
            graph.put(parent, fullLine);
        } else if (lineType.equals(LineType.WHILE) || lineType.equals(LineType.FOR)) {
            condition = line.substring(line.indexOf("(") + 1, line.indexOf(")")) + "-" + j;
            graph.put(parent, fullLine);
            graph.put(fullLine, condition);
            body += "-branch";
            graph.put(fullLine, body);
        }
        return body;
    }

    private static boolean condition(int j, ArrayList<Pair<Integer, LineType>> lines) {
        if (lines.get(j).getValue().equals(LineType.CLOSE)) {
            return j + 1 != lines.size() && (lines.get(j + 1).getValue().equals(LineType.ELSE_IF) || lines.get(j + 1).getValue().equals(LineType.ELSE));
        }
        return true;
    }

    private static int solve(int j, ArrayList<Pair<Integer, LineType>> lines, Graph graph, String parent, HashMap<Integer, String> map) {
        String body = construct(parent, graph, map.get(lines.get(j).getKey()), j, lines.get(j).getValue());
        j++;
        while (condition(j, lines)) {
            if (lines.get(j).getValue().equals(LineType.CLOSE)) {
                j++;
            }
            LineType cur = lines.get(j).getValue();
            if (cur.equals(LineType.STATEMENT)) {
                graph.put(body, cur + "-" + j);
                j++;
            } else if (cur.equals(LineType.ELSE) || cur.equals(LineType.ELSE_IF)) {
                j = solve(j, lines, graph, parent, map);
            } else {
                j = solve(j, lines, graph, body, map);
                j++;
            }
        }
        return j;
    }

    private static HashMap<Integer, String> createMap(File file) throws FileNotFoundException {
        HashMap<Integer, String> map = new HashMap<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            map.put(map.size() + 1, line);
        }
        scanner.close();
        return map;
    }

    public static ArrayList<ArrayList<Graph>> generateGraphs(File folder) throws FileNotFoundException {
        ArrayList<ArrayList<Graph>> graphs = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (!listOfFiles[i].getName().contains(".DS_Store") && listOfFiles[i].isFile() && !listOfFiles[i].getName().contains("RefCode")) {
                File file = listOfFiles[i];
                //System.out.println(file.getName());
                try {
                    HashMap<Integer, String> map = createMap(file);
                    ArrayList<ArrayList<Pair<Integer, LineType>>> lines = LineConverter.convert(file);
                    ArrayList<Graph> graph = new ArrayList<>();
                    for (ArrayList<Pair<Integer, LineType>> assessment : lines) {
                        Graph g = new Graph();
                        String parent = "start";
                        for (int j = 0; j < assessment.size(); j++) {
                            if (assessment.get(j).getValue().equals(LineType.STATEMENT)) {
                                g.put(parent, LineType.STATEMENT + "-" + assessment.get(j).getKey());
                            } else {
                                j = solve(j, assessment, g, parent, map);
                            }
                        }
                        graph.add(g.clone());
                    }
                    graphs.add((ArrayList<Graph>) graph.clone());
                } catch (BracesNotMatchException e) {
                    System.out.println(file.getName() + " not done.");
                }
            }
        }
        return graphs;
    }

    public static ArrayList<ArrayList<Graph>> generateGraphs(String folderName) throws FileNotFoundException {
        ArrayList<ArrayList<Graph>> graphs = new ArrayList<>();
        File folder = new File(folderName);
        return generateGraphs(folder);
    }
}
