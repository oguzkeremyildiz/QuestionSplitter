package org.example;

import java.util.*;

public class Graph {

    private static HashMap<String, ArrayList<String>> graph;
    public Graph() {
        Graph.graph = new HashMap<>();
    }

    public void put(String from, String to) {
        if (!graph.containsKey(from)) {
            graph.put(from, new ArrayList<>());
        }
        graph.get(from).add(to);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String node : graph.keySet()) {
            str.append(node).append(" Edges: \n");
            for (int i = 0; i < graph.get(node).size(); i++) {
                str.append(graph.get(node).get(i)).append(",").append("\n");
            }
            str.append("\n");
        }
        return str.toString();
    }
}
