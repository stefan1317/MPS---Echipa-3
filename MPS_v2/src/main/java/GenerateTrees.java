import main.java.Operations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class GenerateTrees {

    private static Logger log = LoggerFactory.getLogger(GenerateTrees.class);
    private Operations operations;

    public GenerateTrees() {
        operations = new Operations();
    }

    public double firstEquation(ArrayList<Double> testValues) {

        double firstOp, secondOp, thirdOp, fourthOp, fifthOp, sixthOp, seventhOp;

        log.info("First Tree | Calculate the mean of first five even positions in dataset");
        ArrayList<Double> firstEquationValues = new ArrayList<>();
        for (int i = 0; i < 10; i += 2) {
            firstEquationValues.add(testValues.get(i));
        }
        firstOp = operations.mean(firstEquationValues);

        log.info("First Tree | Calculate the square mean of first five odd positions in dataset");
        ArrayList<Double> secondEquationValues = new ArrayList<>();
        for (int i = 1; i < 10; i += 2) {
            secondEquationValues.add(testValues.get(i));
        }
        secondOp = operations.squareMean(secondEquationValues);

        log.info("First Tree | Calculate the multiplication of tenth and fourteenth positions in dataset");
        ArrayList<Double> thirdEquationValues = new ArrayList<>(Arrays.asList
                (testValues.get(10), testValues.get(14)));
        thirdOp = operations.prod(thirdEquationValues);

        log.info("First Tree | Calculate the power of twelfth and thirteenth positions in dataset");
        fourthOp = operations.power(testValues.get(12), testValues.get(13));


        log.info("First Tree | Calculate the harmonic mean for fourteenth position and two of the results");
        ArrayList<Double> fourthEquationValues = new ArrayList<>(Arrays.asList(testValues.get(14), firstOp, fourthOp));
        fifthOp = operations.harmonicMean(fourthEquationValues);

        log.info("First Tree | Calculate |a - b| / (a + b) function for two of the results");
        sixthOp = operations.difDivSum(secondOp, thirdOp);

        log.info("First Tree | Calculate half of one result");
        seventhOp = operations.addHalf(fifthOp);

        log.info("First Tree | Calculate the root of the tree: maximum of two results");
        ArrayList<Double> fifthEquationValues = new ArrayList<>(Arrays.asList(sixthOp, seventhOp));
        return operations.max(fifthEquationValues);

    }

    public double secondEquation(ArrayList<Double> testValues) {

        double firstOp, secondOp, thirdOp, fourthOp, fifthOp, sixthOp,
                seventhOp, eighthOp, ninthOp;

        log.info("");
        ArrayList<Double> firstEquationValues = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            firstEquationValues.add(testValues.get(i));
        }
        firstOp = operations.geometricMean(firstEquationValues);

        log.info("");
        ArrayList<Double> secondEquationValues = new ArrayList<>();
        for (int i = 3; i < 6; i++) {
            secondEquationValues.add(testValues.get(i));
        }
        secondOp = operations.prod(secondEquationValues);

        log.info("");
        ArrayList<Double> thirdEquationValues = new ArrayList<>();
        for (int i = 6; i < 9; i++) {
            thirdEquationValues.add(testValues.get(i));
        }
        thirdOp = operations.squareMean(thirdEquationValues);

        log.info("");
        ArrayList<Double> fourthEquationValues = new ArrayList<>();
        for (int i = 9; i < 12; i++) {
            fourthEquationValues.add(testValues.get(i));
        }
        fourthOp = operations.min(fourthEquationValues);

        log.info("");
        ArrayList<Double> fifthEquationValues = new ArrayList<>();
        for (int i = 12; i < 15; i++) {
            fifthEquationValues.add(testValues.get(i));
        }
        fifthOp = operations.mean(fifthEquationValues);

        log.info("");
        sixthOp = operations.multiplyBy2(secondOp);

        log.info("");
        seventhOp = operations.difDivSum(fourthOp, fifthOp);

        log.info("");
        ArrayList<Double> sixthEquationValues = new ArrayList<>(Arrays.asList(firstOp, thirdOp, fifthOp));
        eighthOp = operations.harmonicMean(sixthEquationValues);

        log.info("");
        ninthOp = operations.difDivSum(sixthOp, eighthOp);

        log.info("");
        return operations.power(seventhOp, ninthOp);
    }

    public double thirdEquation(ArrayList<Double> testValues) {

        double firstOp, secondOp, thirdOp, fourthOp, fifthOp, sixthOp,
                seventhOp, eighthOp, ninthOp;

        Random random = new Random();
        int randomValue1 = random.nextInt(15);
        int randomValue2 = random.nextInt(15);
        int randomValue3 = random.nextInt(15);
        int randomValue4 = random.nextInt(15);
        int randomValue5 = random.nextInt(15);
        Set<Integer> usedValues = new HashSet<>(Set.of(randomValue1, randomValue2, randomValue3,
                randomValue4, randomValue5));

        log.info("");
        ArrayList<Double> firstEquationValues = new ArrayList<>(List.of(testValues.get(randomValue1),
                testValues.get(randomValue2), testValues.get(randomValue3), testValues.get(randomValue4),
                testValues.get(randomValue5)));
        firstOp = operations.mean(firstEquationValues);

        log.info("");
        ArrayList<Double> secondEquationValues = new ArrayList<>();
        for (int i  = 0; i < 15; i++) {
            if (!usedValues.contains(i) && i < 6) {
                usedValues.add(i);
                secondEquationValues.add(testValues.get(i));
            }
        }
        secondOp = operations.prod(secondEquationValues);

        log.info("");
        ArrayList<Double> thirdEquationValues = new ArrayList<>();
        for (int i  = 0; i < 15; i++) {
            if (!usedValues.contains(i)) {
                usedValues.add(i);
                thirdEquationValues.add(testValues.get(i));
            }
        }
        thirdOp = operations.max(thirdEquationValues);

        log.info("");
        fourthOp = operations.addHalf(firstOp);

        log.info("");
        fifthOp = operations.half(secondOp);

        log.info("");
        sixthOp = operations.difDivSum(firstOp, thirdOp);

        log.info("");
        seventhOp = operations.square(fifthOp);

        log.info("");
        ArrayList<Double> fourthEquationValues = new ArrayList<>(List.of(fourthOp, sixthOp));
        eighthOp = operations.geometricMean(fourthEquationValues);

        log.info("");
        ninthOp = operations.multiplyBy2(eighthOp);

        log.info("");
        return operations.add(seventhOp, ninthOp);
    }

    public double fourthEquation(ArrayList<Double> testValues) {

        double firstOp, secondOp, thirdOp, fourthOp, fifthOp, sixthOp,
                seventhOp, eighthOp;

        log.info("");
        ArrayList<Double> firstEquationValues = new ArrayList<>();
        for (int i  = 0; i < 15; i++) {
            if (i > 7 && i <= 12) {
                firstEquationValues.add(testValues.get(i));
            }
        }
        firstOp = operations.prod(firstEquationValues);

        log.info("");
        ArrayList<Double> secondEquationValues = new ArrayList<>();
        for (int i  = 0; i < 8; i++) {
            if (i % 2 == 1) {
                secondEquationValues.add(testValues.get(i));
            }
        }
        secondOp = operations.min(secondEquationValues);

        log.info("");
        ArrayList<Double> thirdEquationValues = new ArrayList<>(List.of(testValues.get(2), testValues.get(4),
                testValues.get(13), testValues.get(14)));
        thirdOp = operations.geometricMean(thirdEquationValues);

        log.info("");
        fourthOp = operations.add(testValues.get(0), testValues.get(14));

        log.info("");
        fifthOp = operations.multiplyBy2(testValues.get(6));

        log.info("");
        ArrayList<Double> fourthEquationValues = new ArrayList<>(List.of(firstOp, fourthOp, fifthOp));
        sixthOp = operations.max(fourthEquationValues);

        log.info("");
        seventhOp = operations.square(thirdOp);

        log.info("");
        eighthOp = operations.power(seventhOp, secondOp);

        log.info("");
        return operations.difDivSum(sixthOp, eighthOp);

    }

    public boolean isPrim(int n) {
        if (n < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public double fifthEquation(ArrayList<Double> testValues) {

        double firstOp, secondOp, thirdOp, fourthOp, fifthOp, sixthOp,
                seventhOp, eighthOp;

        log.info("");
        ArrayList<Double> firstEquationValues = new ArrayList<>();
        for (int i  = 0; i < 15; i++) {
            if (isPrim(i)) {
                firstEquationValues.add(testValues.get(i));
            }
        }
        firstOp = operations.squareMean(firstEquationValues);

        log.info("");
        ArrayList<Double> secondEquationValues = new ArrayList<>();
        for (int i  = 0; i < 15; i++) {
            if (i % 3 == 0) {
                secondEquationValues.add(testValues.get(i));
            }
        }
        secondOp = operations.harmonicMean(secondEquationValues);

        log.info("");
        thirdOp = operations.difDivSum(testValues.get(4), testValues.get(10));

        log.info("");
        fourthOp = operations.half(testValues.get(1));

        log.info("");
        fifthOp = operations.multiplyBy2(testValues.get(14));

        log.info("");
        sixthOp = operations.power(testValues.get(4), testValues.get(8));

        log.info("");
        ArrayList<Double> thirdEquationValues = new ArrayList<>(List.of(secondOp, thirdOp, fourthOp));
        seventhOp = operations.max(thirdEquationValues);

        log.info("");
        eighthOp = operations.add(fifthOp, sixthOp);

        log.info("");
        ArrayList<Double> fourthEquationValues = new ArrayList<>(List.of(firstOp, seventhOp, eighthOp,
                secondOp));
        return operations.prod(fourthEquationValues);
    }
}