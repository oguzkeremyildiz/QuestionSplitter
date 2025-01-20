package org.example;

import java.util.ArrayList;

public class BracesNotMatchException extends Exception {
    private final ArrayList<String> lines;

    public BracesNotMatchException(ArrayList<String> lines) {
        this.lines = lines;
    }

    public BracesNotMatchException() {
        this.lines = new ArrayList<>();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            stringBuilder.append(line).append("\n");
        }
        return "Braces Does not match: " + stringBuilder;
    }
}
