package org.example;

import java.util.*;

public class ControlFlowGraph extends Graph {
    private static Stack<String> ifStack;
    public ControlFlowGraph() {
        super();
        ControlFlowGraph.ifStack = new Stack<>();
    }

    public void add(String node) {
        ifStack.add(node);
    }

    public String get() {
        return ifStack.peek();
    }

    public void pop() {
        ifStack.pop();
    }
}
