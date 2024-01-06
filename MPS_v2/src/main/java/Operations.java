package main.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

public class Operations {

    public ArrayList<String> oneArgFunctions;
    public ArrayList<String> twoArgFunctions;
    public ArrayList<String> multipleArgFunctions;

    public Operations() {
        oneArgFunctions = new ArrayList<>(Arrays.asList("square", "multiplyBy2", "addHalf", "half"));
        twoArgFunctions = new ArrayList<>(Arrays.asList("difDivSum", "add", "power"));
        multipleArgFunctions = new ArrayList<>(Arrays.asList("max", "min", "multiply", "mean",
                "geometricMean", "harmonicMean", "squareMean"));
    }

    public double max(ArrayList<Double> testValues) {
        return Collections.max(testValues);
    }

    public double min(ArrayList<Double> testValues) {
        return Collections.min(testValues);
    }

    public double multiply(ArrayList<Double> testValues) {
        double prod = 1;
        for (double value : testValues) {
            prod *= value;
        }
        return prod;
    }

    public double mean(ArrayList<Double> testValues) {
        double sum = 0;
        for (double value : testValues) {
            sum += value;
        }
        return sum / testValues.size();
    }

    public double geometricMean(ArrayList<Double> testValues) {
        double geometricMean = 1;
        for (double value : testValues) {
            geometricMean *= value;
        }
        return Math.pow(geometricMean, 1.0 / testValues.size());
    }

    public double harmonicMean(ArrayList<Double> testValues) {
        double armonicMean = 0;
        for (double value : testValues) {
            armonicMean += 1 / value;
        }
        return testValues.size() / armonicMean;
    }

    public double squareMean(ArrayList<Double> testValues) {
        double squareMean = 0;
        for (double value : testValues) {
            squareMean += value * value;
        }
        return Math.sqrt(squareMean / testValues.size());
    }

    public double difDivSum(double a, double b) {
        return Math.abs(a - b) / (a + b);
    }

    public double power(double a, double b) {
        return Math.pow(a, b);
    }

    public double add(double a, double b) {
        return a + b < 0.95 ? a + b : a + b - 0.95;
    }

    public double square(double a) {
        return a * a;
    }

    public double multiplyBy2(double a) {
        return a * 2;
    }

    public double addHalf(double a) {
        return a + a / 2 < 1 ? (a + a / 2) : a;
    }

    public double half(double a) {
        return a / 2;
    }

}
