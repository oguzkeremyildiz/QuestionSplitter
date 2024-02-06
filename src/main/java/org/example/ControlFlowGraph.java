package org.example;

import java.util.*;

public class ControlFlowGraph {

    private static HashMap<Integer, ArrayList<Integer>> graph;

    public ControlFlowGraph() {
        ControlFlowGraph.graph = new HashMap<>();
    }

    public void put(int from, int to) {
        if (!graph.containsKey(from)) {
            graph.put(from, new ArrayList<>());
        }
        graph.get(from).add(to);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int node : graph.keySet()) {
            str.append(node).append(" Edges: \n");
            for (int i = 0; i < graph.get(node).size(); i++) {
                str.append(graph.get(node).get(i));
                if (i + 1 != graph.get(node).size()) {
                    str.append(", ");
                }
            }
            str.append("\n");
        }
        return str.toString();
    }
}
