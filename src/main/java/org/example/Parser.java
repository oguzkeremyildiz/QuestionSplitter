package org.example;

import java.util.HashSet;

public class Parser {

    public static HashSet<Integer> types(String line) {
        HashSet<Integer> types = new HashSet<>();
        if (line.contains("}")) {
            types.add(0);
        }
        if (line.contains("if")) {
            types.add(1);
        }
        if (line.contains("else")) {
            types.add(2);
        }
        if (line.contains("while")) {
            types.add(3);
        }
        if (line.contains("for")) {
            types.add(4);
        }
        return types;
    }
}
