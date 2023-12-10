package org.example;

import java.util.ArrayList;

public class DummyCalculator implements PointCalculator {

    @Override
    public double calculate(ArrayList<Integer> splits, ArrayList<String> file) {
        return Math.random();
    }

    @Override
    public int refCodeSize() {
        return (int )(Math.random() * 6);
    }
}
