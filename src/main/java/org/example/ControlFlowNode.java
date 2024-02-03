package org.example;

public class ControlFlowNode {

    private static int line;

    public ControlFlowNode(int line) {
        ControlFlowNode.line = line;
    }

    public int getLine() {
        return line;
    }

    @Override
    public int hashCode() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        ControlFlowNode pair = (ControlFlowNode) o;
        return line == pair.getLine();
    }
}
