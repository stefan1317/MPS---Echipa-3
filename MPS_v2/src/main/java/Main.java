import main.java.CsvReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        CsvReader csvReader = new CsvReader();
        FilesFinder filesFinder = new FilesFinder();

        //Gets file path from command line as argument
//        String GlobalTrain = args[0];
//        String LUTTrain = args[1];

        String GlobalTest = "MPS_v2\\src\\main\\resources\\GlobalTest.csv";
        String LUTTest = "MPS_v2\\src\\main\\resources\\LUTTest.csv";

        Map<Integer, List<Double>> lutTest = csvReader.readDataFromCsv(LUTTest);
        Map<Integer, List<Double>> globalTest = csvReader.readDataFromCsv(GlobalTest);

        Map<String, ArrayList<Pixel>> csvFilesData = filesFinder.getCsvFilesData(
                "MPS_v2/src/main/resources/local/test");
        if (csvFilesData.isEmpty()) {
            throw new RuntimeException("Wrong directory");
        }
        ArrayList<String> treeFiles = filesFinder.getTreeFiles("MPS_v2/src/main/resources");
        if (treeFiles.isEmpty()) {
            throw new RuntimeException("Wrong directory");
        }


//        GenerateTrees generateTrees = new GenerateTrees(globalTest);
//        RunTrees runTrees = new RunTrees(globalTest);
//
//        try {
//            generateTrees.generateTrees();
//            runTrees.runTrees();
//        log.info("The complexity for the trees generation is O(n)");
//        }
//        catch (NoSuchMethodException | InvocationTargetException
//               | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        Testing testing = new Testing();
//        testing.check();

        int nrCores = RunTrees.getNrCores();
        Map<String, Double> treesFMeasures = new HashMap<>();
        log.info("Processing csv files in parallel...");

        for (String treePath : treeFiles) {

            AtomicInteger inQueue = new AtomicInteger(0);
            ExecutorService tpe = Executors.newFixedThreadPool(nrCores);
            ArrayList<GroundTruth> groundTruths = new ArrayList<>();

            int i = 0;

            for (Map.Entry<String, ArrayList<Pixel>> entry : csvFilesData.entrySet()) {
                i++;
                if (i > 5) {break;}
                inQueue.incrementAndGet();
                tpe.submit(new ProcessCsvFiles(tpe, inQueue, treePath,
                        entry.getValue(), groundTruths));
            }
            while (!tpe.isShutdown()) {}
            ArrayList<Double> fMeasures = new ArrayList<>();
            for (GroundTruth groundTruth : groundTruths) {
                double fMeasure = groundTruth.truePositive / (groundTruth.truePositive + 0.5 *
                        (groundTruth.falsePositive + groundTruth.falseNegative));
                fMeasures.add(fMeasure);
            }
            treesFMeasures.put(treePath, new main.java.Operations().mean(fMeasures));
        }
        try {
            FileWriter fWriter = new FileWriter("MPS_v2\\src\\main\\resources\\FMeasures");
            for (Map.Entry<String, Double> entry : treesFMeasures.entrySet()) {
                fWriter.append(entry.getKey()).append(" ").append(entry.getValue().toString()).append("\n");
            }
            fWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(treesFMeasures);
    }


}

class ProcessCsvFiles implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(ProcessCsvFiles.class);
    private ExecutorService tpe;
    private AtomicInteger inQueue;
    private String treePath;
    private ArrayList<Pixel> imageInfo;
    private ArrayList<GroundTruth> groundTruths;

    public ProcessCsvFiles(ExecutorService tpe, AtomicInteger inQueue, String treePath,
                           ArrayList<Pixel> imageInfo, ArrayList<GroundTruth> groundTruths) {
        this.tpe = tpe;
        this.inQueue = inQueue;
        this.treePath = treePath;
        this.imageInfo = imageInfo;
        this.groundTruths = groundTruths;
    }

    @Override
    public void run() {
        RunTreesLocal runTreesLocal = new RunTreesLocal();
        GroundTruth groundTruth = runTreesLocal.runTreesLocal(treePath, imageInfo);
        synchronized (groundTruths) {
            groundTruths.add(groundTruth);
        }
        int left = inQueue.decrementAndGet();
        if (left == 0) {
            tpe.shutdown();
        }
    }
}
