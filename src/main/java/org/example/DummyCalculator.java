package org.example;

public class DummyCalculator implements PointCalculator{

    @Override
    public double calculate() {
        return Math.random();
    }
}
