import main.java.Operations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class RunTrees {

    private static Logger log = LoggerFactory.getLogger(GenerateTrees.class);
    private static Map<Integer, List<Double>> globalFile;
    private static GenerateTrees generateTrees;

    public RunTrees(Map<Integer, List<Double>> globalFile) {
        generateTrees = new GenerateTrees();
        this.globalFile = globalFile;
    }

    public int getNrCores() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.availableProcessors();
    }

    public void generateTrees() throws IOException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {

        // get the number of cores to run the trees execution in parallel
        int nrCores = getNrCores();

        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores);

        log.info("Generating trees...");

        // generate 100 trees in parallel
        for (int i = 1; i <= 100; i++) {
            inQueue.incrementAndGet();
            ArrayList<Double> testValues = new ArrayList<>();
            testValues.addAll(globalFile.get(1));
            tpe.submit(new GenerateTree(tpe, inQueue, testValues, i, generateTrees));
        }

    }

    public void runTrees() throws IOException {

        // get the number of cores to run the trees execution in parallel
        int nrCores = getNrCores();

        log.info("Running trees with pixels values...");
        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores);

        // add each row as a task to run a tree with all dataset values in parallel
        for (int i = 1; i <= 100; i++) {
            FileWriter fWriter = new FileWriter("MPS_v2\\src\\main\\resources\\TreeNr"
                    + i + "Values");

            for (int j = 1; j <= globalFile.size(); j++) {
                inQueue.incrementAndGet();
                ArrayList<Double> testValues = new ArrayList<>();
                testValues.addAll(globalFile.get(j));
                tpe.submit(new RunTree(tpe, inQueue, testValues,
                        i, j, fWriter));
            }
        }
        tpe.shutdown();
    }
}


class RunTree implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(GenerateTrees.class);
    private ExecutorService tpe;
    private AtomicInteger inQueue;
    private final ArrayList<Double> testValues;
    private final int row;
    private final int fileIndex;
    private final FileWriter fWriter;

    public RunTree(ExecutorService tpe, AtomicInteger inQueue, ArrayList<Double> testValues,
                   int fileIndex, int row, FileWriter fWriter) {
        this.tpe = tpe;
        this.inQueue = inQueue;
        this.testValues = testValues;
        this.fileIndex = fileIndex;
        this.row = row;
        this.fWriter = fWriter;
    }

    public double applyFunction(int nrArgs, ArrayList<Double> args, String function)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (nrArgs == 1) {
            Class<?> cls = Operations.class;
            Method method = cls.getMethod(function, double.class);
            return (double) method.invoke(new Operations(), args.get(0));
        } else if (nrArgs == 2) {
            Class<?> cls = Operations.class;
            Method method = cls.getMethod(function, double.class, double.class);
            return (double) method.invoke(new Operations(), args.get(0), args.get(1));
        } else {
            Class<?> cls = Operations.class;
            Method method = cls.getMethod(function, ArrayList.class);
            return (double) method.invoke(new Operations(), args);
        }
    }

    public double calculateTreeValue(BufferedReader reader) throws IOException,
            InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String line;
        Random random = new Random();
        ArrayList<Double> currentLevel = new ArrayList<>();
        currentLevel.addAll(testValues);
        ArrayList<Double> newLevel = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            newLevel.clear();
            String[] tokens = line.split(" ");

            for (int i = 0; i < tokens.length; i += 2) {
                int nrArgs = Integer.parseInt(tokens[i + 1]);
                ArrayList<Double> args = new ArrayList<>();
                for (int j = 0; j < nrArgs; j++) {
                    args.add(currentLevel.remove(random.nextInt(currentLevel.size())));
                }
                newLevel.add(applyFunction(nrArgs, args, tokens[i]));
            }
            currentLevel.addAll(newLevel);
        }
        return newLevel.get(0);
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    "MPS_v2\\src\\main\\resources\\TreeNr" + fileIndex));
            double value = calculateTreeValue(reader);
            synchronized (fWriter) {
                fWriter.append(row + " " + value + "\n");
            }
            int left = inQueue.decrementAndGet();
            if (left == 0) {
                reader.close();
                fWriter.close();
            }
        } catch (IOException | InvocationTargetException
                 | NoSuchMethodException | IllegalAccessException e) {
            log.error("Can not write, read or close a file or wrong call of a function");
            throw new RuntimeException(e);
        }
    }
}


class GenerateTree implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(GenerateTrees.class);
    private ExecutorService tpe;
    private AtomicInteger inQueue;
    private final ArrayList<Double> testValues;
    private int indexFile;
    private GenerateTrees generateTrees;


    public GenerateTree(ExecutorService tpe, AtomicInteger inQueue, ArrayList<Double> testValues,
                        int indexFile, GenerateTrees generateTrees) {
        this.tpe = tpe;
        this.inQueue = inQueue;
        this.testValues = testValues;
        this.indexFile = indexFile;
        this.generateTrees = generateTrees;
    }

    @Override
    public void run() {
        try {
            generateTrees.generateTree(testValues, indexFile);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException
                 | IOException e) {
            log.error("Error while generating tree");
        }
        int left = inQueue.decrementAndGet();
        if (left == 0) {
            tpe.shutdown();
        }
    }
}
