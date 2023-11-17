package main.java;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        CsvReader csvReader = new CsvReader();

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

        GenerateTrees trees = new GenerateTrees(globalTrain);
        System.out.println(trees.generateFirstTrees());
    }
}
