import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class RunTrees {

    private Map<Integer, List<Double>> globalFile;
    private GenerateTrees generateTrees;

    public RunTrees(Map<Integer, List<Double>> globalFile) {
        generateTrees = new GenerateTrees();
        this.globalFile = globalFile;
    }

    public int getNrCores() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.availableProcessors();
    }

    public void runFirstTree() {

        // get the number of cores to run the trees execution in parallel
        int nrCores = getNrCores();

        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores);

        // add each row as a task to run n trees in parallel
        for (int i = 1; i <= globalFile.size(); i++) {
            inQueue.incrementAndGet();
            tpe.submit(new RunTree(tpe, inQueue, (ArrayList<Double>) globalFile.get(i), generateTrees::firstEquation));
        }
    }

    public void runSecondTree() {

        // get the number of cores to run the trees execution in parallel
        int nrCores = getNrCores();

        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores - 1);

        // add each row as a task to run n trees in parallel
        for (int i = 1; i <= globalFile.size(); i++) {
            inQueue.incrementAndGet();
            tpe.submit(new RunTree(tpe, inQueue, (ArrayList<Double>) globalFile.get(i), generateTrees::secondEquation));
        }
    }

    public void runThirdTree() {

        // get the number of cores to run the trees execution in parallel
        int nrCores = getNrCores();

        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores - 1);

        // add each row as a task to run n trees in parallel
        for (int i = 1; i <= globalFile.size(); i++) {
            inQueue.incrementAndGet();
            tpe.submit(new RunTree(tpe, inQueue, (ArrayList<Double>) globalFile.get(i), generateTrees::thirdEquation));
        }
    }

    public void runFourthTree() {

        // get the number of cores to run the trees execution in parallel
        int nrCores = getNrCores();

        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores - 1);

        // add each row as a task to run n trees in parallel
        for (int i = 1; i <= globalFile.size(); i++) {
            inQueue.incrementAndGet();
            tpe.submit(new RunTree(tpe, inQueue, (ArrayList<Double>) globalFile.get(i), generateTrees::fourthEquation));
        }
    }

    public void runFifthTree() {

        // get the number of cores to run the trees execution in parallel
        int nrCores = getNrCores();

        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores - 1);

        // add each row as a task to run n trees in parallel
        for (int i = 1; i <= globalFile.size(); i++) {
            inQueue.incrementAndGet();
            tpe.submit(new RunTree(tpe, inQueue, (ArrayList<Double>) globalFile.get(i), generateTrees::fifthEquation));
        }
    }
}

class RunTree implements Runnable {

    private ExecutorService tpe;
    private AtomicInteger inQueue;
    private ArrayList<Double> testValues;
    Function<ArrayList<Double>, Double> treeEquation;

    public RunTree(ExecutorService tpe, AtomicInteger inQueue, ArrayList<Double> testValues,
                   Function<ArrayList<Double>, Double> treeEquation) {
        this.tpe = tpe;
        this.inQueue = inQueue;
        this.testValues = testValues;
        this.treeEquation = treeEquation;
    }

    @Override
    public void run() {
        double value = treeEquation.apply(testValues);
        System.out.println(value);
        int left = inQueue.decrementAndGet();
        if (left == 0) {
            tpe.shutdown();
        }
    }
}
