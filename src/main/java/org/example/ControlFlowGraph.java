package org.example;

import java.util.*;

public class ControlFlowGraph {

    private static HashMap<ControlFlowNode, ArrayList<ControlFlowNode>> graph;

    public ControlFlowGraph() {
        ControlFlowGraph.graph = new HashMap<>();
    }

    public void put(ControlFlowNode from, ControlFlowNode to) {
        if (!graph.containsKey(from)) {
            graph.put(from, new ArrayList<>());
        }
        graph.get(from).add(to);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (ControlFlowNode node : graph.keySet()) {
            str.append(node).append(": \n");
            for (int i = 0; i < graph.get(node).size(); i++) {
                str.append(graph.get(node).get(i)).append("\n");
            }
        }
        return str.toString();
    }
}
