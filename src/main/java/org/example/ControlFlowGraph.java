package org.example;

import java.util.*;

public class ControlFlowGraph extends Graph {
    private static Stack<String> ifStack;
    private static boolean justElse;
    public ControlFlowGraph() {
        super();
        ControlFlowGraph.justElse = false;
        ControlFlowGraph.ifStack = new Stack<>();
    }

    public void add(String node) {
        ifStack.add(node);
    }

    public String get() {
        return ifStack.peek();
    }

    public boolean isJustElse() {
        return justElse;
    }

    public void change() {
        ControlFlowGraph.justElse = !ControlFlowGraph.justElse;
    }

    public void pop() {
        ifStack.pop();
    }
}
