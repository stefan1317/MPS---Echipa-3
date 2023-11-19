import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class RunTrees {

    private static Logger log = LoggerFactory.getLogger(GenerateTrees.class);
    private Map<Integer, List<Double>> globalFile;
    private static GenerateTrees generateTrees;

    public RunTrees(Map<Integer, List<Double>> globalFile) {
        generateTrees = new GenerateTrees();
        this.globalFile = globalFile;
    }

    public int getNrCores() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.availableProcessors();
    }

    public void runFirstTree() throws IOException{

        // get the number of cores to run the trees execution in parallel
        int nrCores = getNrCores();

        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores);
        FileWriter fWriter = new FileWriter("MPS_v2\\src\\main\\resources\\FirstTree");

        log.info("Running first tree");
        // add each row as a task to run n trees in parallel
        for (int i = 1; i <= globalFile.size(); i++) {
            inQueue.incrementAndGet();
            tpe.submit(new RunTree(tpe, inQueue, (ArrayList<Double>) globalFile.get(i),
                    generateTrees::firstEquation, i,fWriter));
        }

    }

    public void runSecondTree() throws IOException{

        // get the number of cores to run the trees execution in parallel
        int nrCores = getNrCores();

        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores - 1);
        FileWriter fWriter = new FileWriter("MPS_v2\\src\\main\\resources\\SecondTree");

        log.info("Running second tree");
        // add each row as a task to run n trees in parallel
        for (int i = 1; i <= globalFile.size(); i++) {
            inQueue.incrementAndGet();
            tpe.submit(new RunTree(tpe, inQueue, (ArrayList<Double>) globalFile.get(i),
                    generateTrees::secondEquation, i, fWriter));
        }
    }

    public void runThirdTree() throws IOException{

        // get the number of cores to run the trees execution in parallel
        int nrCores = getNrCores();

        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores - 1);
        FileWriter fWriter = new FileWriter("MPS_v2\\src\\main\\resources\\ThirdTree");

        log.info("Running third tree");
        // add each row as a task to run n trees in parallel
        for (int i = 1; i <= globalFile.size(); i++) {
            inQueue.incrementAndGet();
            tpe.submit(new RunTree(tpe, inQueue, (ArrayList<Double>) globalFile.get(i),
                    generateTrees::thirdEquation, i, fWriter));
        }
    }

    public void runFourthTree() throws IOException{

        // get the number of cores to run the trees execution in parallel
        int nrCores = getNrCores();

        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores - 1);
        FileWriter fWriter = new FileWriter("MPS_v2\\src\\main\\resources\\FourthTree");

        log.info("Running fourth tree");
        // add each row as a task to run n trees in parallel
        for (int i = 1; i <= globalFile.size(); i++) {
            inQueue.incrementAndGet();
            tpe.submit(new RunTree(tpe, inQueue, (ArrayList<Double>) globalFile.get(i),
                    generateTrees::fourthEquation, i, fWriter));
        }
    }

    public void runFifthTree() throws IOException{

        // get the number of cores to run the trees execution in parallel
        int nrCores = getNrCores();

        AtomicInteger inQueue = new AtomicInteger(0);
        ExecutorService tpe = Executors.newFixedThreadPool(nrCores - 1);
        FileWriter fWriter = new FileWriter("MPS_v2\\src\\main\\resources\\FifthTree");

        log.info("Running fifth tree");
        // add each row as a task to run n trees in parallel
        for (int i = 1; i <= globalFile.size(); i++) {
            inQueue.incrementAndGet();
            tpe.submit(new RunTree(tpe, inQueue, (ArrayList<Double>) globalFile.get(i),
                    generateTrees::fifthEquation, i, fWriter));
        }
    }
}

class RunTree implements Runnable {

    private static Logger log = LoggerFactory.getLogger(GenerateTrees.class);
    private ExecutorService tpe;
    private AtomicInteger inQueue;
    private ArrayList<Double> testValues;
    Function<ArrayList<Double>, Double> treeEquation;
    private int row;
    private FileWriter fWriter;

    public RunTree(ExecutorService tpe, AtomicInteger inQueue, ArrayList<Double> testValues,
                   Function<ArrayList<Double>, Double> treeEquation, int row, FileWriter fWriter) {
        this.tpe = tpe;
        this.inQueue = inQueue;
        this.testValues = testValues;
        this.treeEquation = treeEquation;
        this.row = row;
        this.fWriter = fWriter;
    }

    @Override
    public void run() {
        double value = treeEquation.apply(testValues);
        try {
            fWriter.append(row + " " + value + "\n");
        } catch (IOException e) {
            log.error("Can not write to file");
            throw new RuntimeException(e);
        }
        int left = inQueue.decrementAndGet();
        if (left == 0) {
            try {
                fWriter.close();
            } catch (IOException e) {
                log.error("Can not close file");
                throw new RuntimeException(e);
            }
            tpe.shutdown();
        }
    }
}
