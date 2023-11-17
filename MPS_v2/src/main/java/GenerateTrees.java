package main.java;

import java.util.*;

public class GenerateTrees {

    private Map<Integer, List<Double>> dataFromCsv;
    private Operations operations;

    public GenerateTrees(Map<Integer, List<Double>> dataFromCsv) {
        this.dataFromCsv = dataFromCsv;
        operations = new Operations();
    }

    // I calculate the value of first tree for each row
    public Map<Integer, Double> generateFirstTrees() {
        Map<Integer, Double> values = new HashMap<>();
        for (int i = 1; i < dataFromCsv.size(); i++) {
            values.put(i, firstEquation((ArrayList<Double>) dataFromCsv.get(i)));
        }
        return values;
    }

    public Map<Integer, Double> generateSecondTrees() {
        Map<Integer, Double> values = new HashMap<>();
        for (int i = 1; i < dataFromCsv.size(); i++) {
            values.put(i, secondEquation((ArrayList<Double>) dataFromCsv.get(i)));
        }
        return values;
    }

    public Map<Integer, Double> generateThirdTrees() {
        Map<Integer, Double> values = new HashMap<>();
        for (int i = 1; i < dataFromCsv.size(); i++) {
            values.put(i, thirdEquation((ArrayList<Double>) dataFromCsv.get(i)));
        }
        return values;
    }

    public Map<Integer, Double> generateFourthTrees() {
        Map<Integer, Double> values = new HashMap<>();
        for (int i = 1; i < dataFromCsv.size(); i++) {
            values.put(i, fourthEquation((ArrayList<Double>) dataFromCsv.get(i)));
        }
        return values;
    }

    public Map<Integer, Double> generateFifthTrees() {
        Map<Integer, Double> values = new HashMap<>();
        for (int i = 1; i < dataFromCsv.size(); i++) {
            values.put(i, fifthEquation((ArrayList<Double>) dataFromCsv.get(i)));
        }
        return values;
    }

    public double firstEquation(ArrayList<Double> testValues) {
        ArrayList<Double> firstEquationValues = new ArrayList<>();
        ArrayList<Double> secondEquationValues = new ArrayList<>();
        ArrayList<Double> thirdEquationValues = new ArrayList<>();
        ArrayList<Double> fourthEquationValues = new ArrayList<>();
        ArrayList<Double> fifthEquationValues = new ArrayList<>();
        double firstOp, secondOp, thirdOp, fourthOp, fifthOp, sixthOp, seventhOp;

        for (int i = 0; i < 10; i += 2) {
            firstEquationValues.add(testValues.get(i));
        }

        for (int i = 1; i < 10; i += 2) {
            secondEquationValues.add(testValues.get(i));
        }

        thirdEquationValues.add(testValues.get(10));
        thirdEquationValues.add(testValues.get(14));

        firstOp = operations.mean(firstEquationValues);
        secondOp = operations.squareMean(secondEquationValues);
        thirdOp = operations.power(testValues.get(12), testValues.get(13));
        fourthOp = operations.prod(thirdEquationValues);

        fourthEquationValues.add(testValues.get(14));
        fourthEquationValues.add(firstOp);
        fourthEquationValues.add(fourthOp);

        fifthOp = operations.armonicMean(fourthEquationValues);
        sixthOp = operations.difDivSum(secondOp, thirdOp);
        seventhOp = operations.addHalf(fifthOp);

        fifthEquationValues.add(sixthOp);
        fifthEquationValues.add(seventhOp);

        return operations.max(fifthEquationValues);

    }

    public double secondEquation(ArrayList<Double> testValues) {
        ArrayList<Double> firstEquationValues = new ArrayList<>();
        ArrayList<Double> secondEquationValues = new ArrayList<>();
        ArrayList<Double> thirdEquationValues = new ArrayList<>();
        ArrayList<Double> fourthEquationValues = new ArrayList<>();
        ArrayList<Double> fifthEquationValues = new ArrayList<>();
        ArrayList<Double> sixthEquationValues = new ArrayList<>();

        double firstOp, secondOp, thirdOp, fourthOp, fifthOp, sixthOp,
                seventhOp, eighthOp, ninthOp;

        for (int i = 0; i < 3; i++) {
            firstEquationValues.add(testValues.get(i));
        }

        for (int i = 3; i < 6; i++) {
            secondEquationValues.add(testValues.get(i));
        }

        for (int i = 6; i < 9; i++) {
            thirdEquationValues.add(testValues.get(i));
        }

        for (int i = 9; i < 12; i++) {
            fourthEquationValues.add(testValues.get(i));
        }

        for (int i = 12; i < 15; i++) {
            fifthEquationValues.add(testValues.get(i));
        }

        firstOp = operations.geometricMean(firstEquationValues);
        secondOp = operations.prod(secondEquationValues);
        thirdOp = operations.squareMean(thirdEquationValues);
        fourthOp = operations.min(fourthEquationValues);
        fifthOp = operations.mean(fifthEquationValues);

        sixthOp = operations.multiplyBy2(secondOp);
        seventhOp = operations.difDivSum(fourthOp, fifthOp);

        sixthEquationValues.add(firstOp);
        sixthEquationValues.add(thirdOp);
        sixthEquationValues.add(fifthOp);

        eighthOp = operations.armonicMean(sixthEquationValues);

        ninthOp = operations.difDivSum(sixthOp, eighthOp);

        return operations.power(seventhOp, ninthOp);
    }

    public double thirdEquation(ArrayList<Double> testValues) {
        Set<Integer> usedValues = new HashSet<>();
        ArrayList<Double> firstEquationValues = new ArrayList<>();
        ArrayList<Double> secondEquationValues = new ArrayList<>();
        ArrayList<Double> thirdEquationValues = new ArrayList<>();
        ArrayList<Double> fourthEquationValues = new ArrayList<>();


        double firstOp, secondOp, thirdOp, fourthOp, fifthOp, sixthOp,
                seventhOp, eighthOp, ninthOp;

        Random random = new Random();
        int randomValue1 = random.nextInt(15);
        int randomValue2 = random.nextInt(15);
        int randomValue3 = random.nextInt(15);
        int randomValue4 = random.nextInt(15);
        int randomValue5 = random.nextInt(15);
        usedValues.add(randomValue1);
        usedValues.add(randomValue2);
        usedValues.add(randomValue3);
        usedValues.add(randomValue4);
        usedValues.add(randomValue5);

        firstEquationValues.add(testValues.get(randomValue1));
        firstEquationValues.add(testValues.get(randomValue2));
        firstEquationValues.add(testValues.get(randomValue3));
        firstEquationValues.add(testValues.get(randomValue4));
        firstEquationValues.add(testValues.get(randomValue5));

        firstOp = operations.mean(firstEquationValues);

        for (int i  = 0; i < 15; i++) {
            if (!usedValues.contains(i) && i < 6) {
                usedValues.add(i);
                secondEquationValues.add(testValues.get(i));
            }
        }
        secondOp = operations.prod(secondEquationValues);
        for (int i  = 0; i < 15; i++) {
            if (!usedValues.contains(i)) {
                usedValues.add(i);
                thirdEquationValues.add(testValues.get(i));
            }
        }
        thirdOp = operations.max(thirdEquationValues);

        fourthOp = operations.addHalf(firstOp);
        fifthOp = operations.half(secondOp);
        sixthOp = operations.difDivSum(firstOp, thirdOp);

        seventhOp = operations.square(fifthOp);
        fourthEquationValues.add(fourthOp);
        fourthEquationValues.add(sixthOp);
        eighthOp = operations.geometricMean(fourthEquationValues);
        ninthOp = operations.multiplyBy2(eighthOp);

        return operations.add(seventhOp, ninthOp);
    }

    public double fourthEquation(ArrayList<Double> testValues) {
        Set<Integer> usedValues = new HashSet<>();
        ArrayList<Double> firstEquationValues = new ArrayList<>();
        ArrayList<Double> secondEquationValues = new ArrayList<>();
        ArrayList<Double> thirdEquationValues = new ArrayList<>();
        ArrayList<Double> fourthEquationValues = new ArrayList<>();


        double firstOp, secondOp, thirdOp, fourthOp, fifthOp, sixthOp,
                seventhOp, eighthOp, ninthOp;


    }

    public double fifthEquation(ArrayList<Double> testValues) {

    }
}