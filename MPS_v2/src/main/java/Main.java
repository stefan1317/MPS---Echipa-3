import main.java.CsvReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {

    private static Logger log = LoggerFactory.getLogger(GenerateTrees.class);

    public static void main(String[] args) {

        CsvReader csvReader = new CsvReader();

        //Gets file path from command line as argument
//        String GlobalTrain = args[0];
//        String LUTTrain = args[1];

        String GlobalTrain = "MPS_v2\\src\\main\\resources\\GlobalTrain.csv";
        String LUTTrain = "MPS_v2\\src\\main\\resources\\LUTTrain.csv";

        /*
            HashMap where the key is the number of the line (for each picture) and the value is a
            List where:
                1) index 0 is the binarization threshold after applying the OtsuT algorithm
                2) index 1 is the binarization threshold after applying the KittlerT algorithm
                ... and so on.
         */
        Map<Integer, List<Double>> lutTrain = csvReader.readDataFromCsv(LUTTrain);
        Map<Integer, List<Double>> globalTrain = csvReader.readDataFromCsv(GlobalTrain);

        RunTrees runTrees = new RunTrees(globalTrain);
        try {
//            runTrees.runFirstTree();
//            runTrees.runSecondTree();
            runTrees.runThirdTree();
//            runTrees.runFourthTree();
//            runTrees.runFifthTree();
        } catch (IOException e) {
            log.error("Could not write to file");
            throw new RuntimeException(e);
        }
        log.info("The complexity for the trees generation is O(n)");
        
        Testing test =  new Testing();
        try {
            Testing.CheckOptimization("MPS_v2\\src\\main\\resources\\FirstTree");
            Testing.CheckOptimization("MPS_v2\\src\\main\\resources\\SecondTree");
            Testing.CheckOptimization("MPS_v2\\src\\main\\resources\\ThirdTree");
            Testing.CheckOptimization("MPS_v2\\src\\main\\resources\\FourthTree");
            Testing.CheckOptimization("MPS_v2\\src\\main\\resources\\FifthTree");
            Testing.printAveragesAboveMean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
