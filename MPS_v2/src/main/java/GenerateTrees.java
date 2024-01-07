import main.java.Operations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class GenerateTrees {

    private static Logger log = LoggerFactory.getLogger(GenerateTrees.class);
    private Operations operations;
    private static Map<Integer, List<Double>> globalFile;


    public GenerateTrees(Map<Integer, List<Double>> globalFile) {
        operations = new Operations();
        this.globalFile = globalFile;
    }

    public double applyFunction(int nrArgs, ArrayList<Double> args, FileWriter fWriter)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Random random = new Random();
        if (nrArgs == 1) {
            String function = operations.oneArgFunctions.get(random
                    .nextInt(operations.oneArgFunctions.size()));
            fWriter.append(function).append(" ").append(String.valueOf(nrArgs)).append(" ");
            Class<?> cls = Operations.class;
            Method method = cls.getMethod(function, double.class);
            return (double) method.invoke(operations, args.get(0));
        } else if (nrArgs == 2) {
            String function = operations.twoArgFunctions.get(random
                    .nextInt(operations.twoArgFunctions.size()));
            fWriter.append(function).append(" ").append(String.valueOf(nrArgs)).append(" ");
            Class<?> cls = Operations.class;
            Method method = cls.getMethod(function, double.class, double.class);
            return (double) method.invoke(operations, args.get(0), args.get(1));
        } else {
            String function = operations.multipleArgFunctions.get(random
                    .nextInt(operations.multipleArgFunctions.size()));
            fWriter.append(function).append(" ").append(String.valueOf(nrArgs)).append(" ");
            Class<?> cls = Operations.class;
            Method method = cls.getMethod(function, ArrayList.class);
            return (double) method.invoke(operations, args);
        }
    }

    public void generateTree(ArrayList<Double> testValues, int indexFile) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, IOException {
        Random random = new Random();
        // the tree has 10 leaves
        for (int i = 0; i < 5; i++) {
            testValues.remove(random.nextInt(15 - i));
        }
        ArrayList<Double> currentLevel = new ArrayList(testValues);
        ArrayList<Double> newLevel = new ArrayList();
        FileWriter fWriter = new FileWriter("MPS_v2\\src\\main\\resources\\TreeNr" + indexFile);
        while (newLevel.size() != 1) {
            newLevel.clear();
            while (currentLevel.size() != 0) {
                int nrArgs;
                if (currentLevel.size() < 5) {
                    nrArgs = random.nextInt(currentLevel.size()) + 1;
                } else {
                    nrArgs = random.nextInt(currentLevel.size() - 4) + 1;
                }
                ArrayList<Double> args = new ArrayList<>();
                for (int i = 0; i < nrArgs; i++) {
                    args.add(currentLevel.remove(random.nextInt(currentLevel.size())));
                }
                newLevel.add(applyFunction(nrArgs, args, fWriter));
            }
            currentLevel.addAll(newLevel);
            fWriter.append("\n");
        }
        fWriter.close();
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
            tpe.submit(new GenerateTree(tpe, inQueue, testValues, i, new GenerateTrees(globalFile)));
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