import main.java.CsvReader;
import java.util.List;
import java.util.Map;

public class Main {
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
        runTrees.runFirstTree();
    }
}
