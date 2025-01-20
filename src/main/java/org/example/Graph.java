package org.example;

import java.util.*;

public class Graph {

    private final HashMap<String, ArrayList<String>> graph;

    public Graph() {
        this.graph = new HashMap<>();
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
            for (int i = 0; i < graph.get(node).size(); i++) {
                str.append(node).append( " -> ").append(graph.get(node).get(i)).append("\n");
            }
        }
        return str.toString();
    }

    public Graph clone() {
        Graph graph = new Graph();
        for (String key : this.graph.keySet()) {
            for (String value : this.graph.get(key)) {
                graph.put(key, value);
            }
        }
        return graph;
    }
}
